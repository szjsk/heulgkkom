package msmgw.heulgkkom.service;


import static msmgw.heulgkkom.model.constant.HttpMethodEnum.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiComponent;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.ApiVersion;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.entity.DomainVersionId;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.repository.ApiComponentRepository;
import msmgw.heulgkkom.repository.ApiDomainRepository;
import msmgw.heulgkkom.repository.ApiPathRepository;
import msmgw.heulgkkom.repository.ApiVersionRepository;
import msmgw.heulgkkom.repository.DomainVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    List<ApiPath> lists = openAPI.getPaths().entrySet().stream()
        .flatMap(o -> {
          List<ApiPath> list = new ArrayList<>();
          PathItem value = o.getValue();
          appendPath(list, serviceId, versionId, o.getKey(), value.getGet(), GET);
          appendPath(list, serviceId, versionId, o.getKey(), value.getPost(), POST);
          appendPath(list, serviceId, versionId, o.getKey(), value.getDelete(), DELETE);
          appendPath(list, serviceId, versionId, o.getKey(), value.getHead(), HEAD);
          appendPath(list, serviceId, versionId, o.getKey(), value.getPut(), PUT);
          appendPath(list, serviceId, versionId, o.getKey(), value.getOptions(), OPTION);
          appendPath(list, serviceId, versionId, o.getKey(), value.getPatch(), PATCH);
          appendPath(list, serviceId, versionId, o.getKey(), value.getTrace(), TRACE);

          return list.stream();
        }).toList();

    apiPathRepository.saveAll(lists);
  }

  private void appendPath(List<ApiPath> list, Long serviceId, Long versionId, String path, Operation value, HttpMethodEnum type) {

    if (Objects.isNull(value)) {
      return;
    }

    list.add(ApiPath.builder()
        .pathKey(concatPathKey(serviceId, type, path))
        .versionId(versionId)
        .path(path)
        .method(type)
        .data(convertObjectToString(value))
        .build());
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

}
