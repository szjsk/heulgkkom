package msmgw.heulgkkom.service;


import static msmgw.heulgkkom.model.constant.HttpMethodEnum.DELETE;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.GET;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.HEAD;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.OPTION;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PATCH;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.POST;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PUT;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.TRACE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiComponent;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.ApiVersion;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.ApiDto;
import msmgw.heulgkkom.model.ApiDto.ApiDtoBuilder;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.repository.ApiComponentRepository;
import msmgw.heulgkkom.repository.ApiPathRepository;
import msmgw.heulgkkom.repository.ApiVersionRepository;
import msmgw.heulgkkom.repository.DomainVersionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiManagerService {

  final private ApiVersionRepository apiVersionRepository;
  final private ApiComponentRepository apiComponentRepository;
  final private ApiPathRepository apiPathRepository;
  final private DomainVersionRepository domainVersionRepository;


  @Transactional
  public SwaggerParseResult parseUri(String location) {
    return new OpenAPIV3Parser().readLocation(location, null, null);
  }

  @Transactional
  public Long parseString(String data, Long serviceId) {
    return saveApiList(new OpenAPIV3Parser().readContents(data), serviceId, data);
  }

  private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


  public List<ApiManagerVersionDto> retrieveApiVersionDomainList(Long serviceId) {

    return apiVersionRepository.findCustomByServiceId(serviceId);
  }

  private Long saveApiList(SwaggerParseResult result, Long serviceId, String orgData) {

    OpenAPI openAPI = result.getOpenAPI();
    Long versionId = saveApiVersion(openAPI.getSpecVersion().name(), serviceId, orgData);
    saveComponents(openAPI, versionId);
    savePath(openAPI, versionId, serviceId);

    return versionId;
  }

  private void savePath(OpenAPI openAPI, Long versionId, Long serviceId) {

    Set<Entry<String, PathItem>> entries = openAPI.getPaths().entrySet();

    List<ApiPath> lists = extractedPathInfo(openAPI, entries)
        .map(o ->
            ApiPath.builder()
                .pathKey(concatPathKey(serviceId, o.getMethod(), o.getPath()))
                .versionId(versionId)
                .path(o.getPath())
                .method(o.getMethod())
                .data(convertObjectToString(o.getOp()))
                .parameter(o.getParameter())
                .requestBody(o.getRequestBody())
                .response(o.getResponse())
                .build()
        )
        .toList();

    apiPathRepository.saveAll(lists);
  }

  private Stream<ApiDto> extractedPathInfo(OpenAPI openAPI, Set<Entry<String, PathItem>> entries) {
    return entries.stream()
        .flatMap(e -> {
          String k = e.getKey();
          PathItem v = e.getValue();
          List<ApiDto> group = new ArrayList<>();

          group.add(printOperation(openAPI, v.getHead(), HEAD, k));
          group.add(printOperation(openAPI, v.getGet(), GET, k));
          group.add(printOperation(openAPI, v.getPost(), POST, k));
          group.add(printOperation(openAPI, v.getDelete(), DELETE, k));
          group.add(printOperation(openAPI, v.getPut(), PUT, k));
          group.add(printOperation(openAPI, v.getOptions(), OPTION, k));
          group.add(printOperation(openAPI, v.getPatch(), PATCH, k));
          group.add(printOperation(openAPI, v.getTrace(), TRACE, k));

          return group.stream();
        })
        .filter(Objects::nonNull);
  }

  private String concatPathKey(Long serviceId, HttpMethodEnum type, String path) {
    return serviceId + "_" + type + "_" + path;
  }

  private void saveComponents(OpenAPI openAPI, Long versionId) {
    if (Objects.isNull(openAPI.getComponents().getSchemas())) {
      return;
    }
    List<ApiComponent> components = openAPI.getComponents().getSchemas().entrySet().stream()
        .map(o -> ApiComponent.builder()
            .versionId(versionId)
            .name(o.getKey())
            .data(convertObjectToString(o.getValue()))
            .build()
        ).toList();

    apiComponentRepository.saveAll(components);
  }

  private static String convertObjectToString(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private Long saveApiVersion(String openApiVersion, Long serviceId, String orgData) {
    ApiVersion apiVersion = ApiVersion.builder()
        .serviceId(serviceId)
        .versionName(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
        .openapiVersion(openApiVersion)
        .orginalData(orgData)
        .created("todo")
        .build();

    ApiVersion save = apiVersionRepository.save(apiVersion);
    return save.getVersionId();
  }


  public void domainVersionRepository(Long domainId, Long versionId) {

    //todo
    Optional<DomainVersion> data = domainVersionRepository.findByDomainId(domainId);

    data.ifPresent(domainVersionRepository::delete);

    DomainVersion dto = DomainVersion.builder()
        .domainId(domainId)
        .versionId(versionId)
        .requested("todo")
        .build();

    domainVersionRepository.save(dto);

  }

  public List<ApiPath> retrieveApiPathList(Long versionId) {
    return apiPathRepository.findAllByVersionIdOrderByPathAscMethodAsc(versionId);
  }


  public List<ApiPathDto> retrieveApiPathByDefault(Long domainId) {
    return apiVersionRepository.findCustomByDomainId(domainId);
  }

  public List<ApiDto> getPathAndParameter(OpenAPI openAPI) {
    Paths paths = openAPI.getPaths();

    Set<Entry<String, PathItem>> entries = paths.entrySet();

    return entries.stream()
        .flatMap(e -> {
          String k = e.getKey();
          PathItem v = e.getValue();
          List<ApiDto> group = new ArrayList<>();

          group.add(printOperation(openAPI, v.getHead(), HEAD, k));
          group.add(printOperation(openAPI, v.getGet(), GET, k));
          group.add(printOperation(openAPI, v.getPost(), POST, k));
          group.add(printOperation(openAPI, v.getDelete(), DELETE, k));
          group.add(printOperation(openAPI, v.getPut(), PUT, k));
          group.add(printOperation(openAPI, v.getOptions(), OPTION, k));
          group.add(printOperation(openAPI, v.getPatch(), PATCH, k));
          group.add(printOperation(openAPI, v.getTrace(), TRACE, k));

          return group.stream();
        })
        .filter(Objects::nonNull)
        .toList();

  }

  private static final String COLON = " : ";
  private static final String SPACE = " ";
  private static final String CHILD_PREFIX = "\tL";
  private static final String CRLF = "\n";

  private ApiDto printOperation(OpenAPI openAPI, Operation op, HttpMethodEnum type, String path) {
    if (op == null) {
      return null;
    }

    ApiDto apiDto = ApiDto.create(path, type, op);

    if (op.getParameters() != null) {
      StringBuilder parameter = new StringBuilder();
      op.getParameters().forEach(o -> {
        if (Objects.isNull(o.getSchema().getType())) {
          printReference(openAPI, o.getSchema(), parameter, EMPTY);
        } else {
          parameter.append(CRLF).append(o.getName()).append(COLON).append(o.getSchema().getType());
        }
      });
      apiDto.setParameter(parameter.toString());
    }
    if (op.getRequestBody() != null) {
      apiDto.setRequestBody(printRequestBody(openAPI, op.getRequestBody()));
    }
    apiDto.setResponse(printResponses(openAPI, op.getResponses()));

    return apiDto;
  }

  private String printRequestBody(OpenAPI openAPI, RequestBody body) {
    StringBuilder reqBody = new StringBuilder();
    body.getContent().forEach((k, v) -> {
      reqBody.append(CRLF).append("content Type : ").append(k);
      printReference(openAPI, v.getSchema(), reqBody, EMPTY);
    });
    return reqBody.toString();
  }

  private String printResponses(OpenAPI openAPI, ApiResponses res) {
    StringBuilder responses = new StringBuilder();
    res.forEach((k, v) -> {
      responses.append(CRLF).append(k).append(SPACE).append(v.getDescription());
      if (v.getContent() != null) {
        v.getContent().forEach((name, media) -> {
          responses.append(CRLF).append(name);
          printReference(openAPI, media.getSchema(), responses, EMPTY);
        });
      }
    });

    return responses.toString();
  }

  private void printReference(OpenAPI openAPI, Schema<?> schema, StringBuilder str, String childPrefix) {
    if (schema instanceof ArraySchema arraySchema) {
      printReference(openAPI, arraySchema.getItems(), str, childPrefix + CHILD_PREFIX);
    }
    String componentName = getComponentName(schema.get$ref());
    str.append(CRLF).append(childPrefix).append(" [DTO] ").append(componentName);

    if (componentName != null) {
      Schema objSchema = openAPI.getComponents().getSchemas().get(componentName);

      if (objSchema instanceof ArraySchema arraySchema) {
        printReference(openAPI, arraySchema.getItems(), str, childPrefix + CHILD_PREFIX);
        return;
      } else if (objSchema != null) {
        objSchema.getProperties()
            .forEach((k, v) -> {
              if (v instanceof Schema<?>) {
                str.append(CRLF).append(childPrefix).append(SPACE).append(k);
                str.append(COLON).append(((Schema<?>) v).getType());

                if (!Objects.isNull(((Schema<?>) v).getFormat())) {
                  str.append(SPACE).append(((Schema<?>) v).getFormat());
                }
              } else {
                str.append(CRLF).append(childPrefix).append(SPACE).append(k).append(COLON).append(v.getClass().getSimpleName());
              }
            });
      }

    }
  }

  private String getComponentName(String s) {
    if (StringUtils.isBlank(s)) {
      return EMPTY;
    }
    String prefix = "#/components/schemas/";
    if (s.startsWith(prefix)) {
      return s.substring(prefix.length());
    }
    return EMPTY;
  }


}
