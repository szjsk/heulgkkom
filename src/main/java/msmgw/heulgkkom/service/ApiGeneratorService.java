package msmgw.heulgkkom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiComponent;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.ApiGeneratorDto;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;
import msmgw.heulgkkom.repository.AllowApiRepository;
import msmgw.heulgkkom.repository.ApiComponentRepository;
import msmgw.heulgkkom.repository.ApiDomainRepository;
import msmgw.heulgkkom.repository.ApiServiceRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGeneratorService {

  private final ApiDomainRepository apiDomainRepository;
  private final ApiServiceRepository apiServiceRepository;
  private final ApiComponentRepository apiComponentRepository;

  private final AllowApiRepository allowApiRepository;

  private static final String DOMAIN_ID_VARIABLE = "{{domainId}}";
  private static final String CHANGE_DOMAIN_VARIABLE = "{{change-domain}}";

  public String extractGradleTextBy(String serviceName, String group) {

    ApiService api = apiServiceRepository.findByServiceNameAndStatus(serviceName, ServiceStatusEnum.USE)
        .orElseThrow(() -> new IllegalArgumentException("can not find service : " + serviceName));

    ApiDomain apiDomain = apiDomainRepository.findByServiceIdAndGroup(api.getServiceId(), group)
        .orElseThrow(() -> new IllegalArgumentException("can not find domain : " + group));

    String serviceGradleText = api.getServiceGradle();
    return serviceGradleText.replace(DOMAIN_ID_VARIABLE, apiDomain.getDomainId().toString())
        .replace(CHANGE_DOMAIN_VARIABLE, "http://localhost:8079"); //todo change heulgkkom server url

  }

  public String makeSpec(Long domainId) throws JsonProcessingException {

    ApiDomain domain = apiDomainRepository.findById(domainId)
        .orElseThrow(() -> new IllegalArgumentException("can not find domain : " + domainId));

    List<ApiGeneratorDto> dto = allowApiRepository.findCustomByPaths(domain.getDomainId(), domain.getGroup());

    Set<Long> versionIds = dto.stream()
        .map(ApiGeneratorDto::getVersionId)
        .collect(Collectors.toSet());

    Map<String, ApiComponent> components = apiComponentRepository.findAllByVersionIdIn(versionIds)
        .stream()
        .collect(Collectors.toMap(ApiComponent::getName, Function.identity(), (a, b) -> a)); //todo 중복건 처리

    return createOpenApi(dto, components);

  }
  private final String PREFIX_SCHEMA = "#/components/schemas/";
  private final static ObjectMapper mapper = Yaml.mapper();
  private String createOpenApi(List<ApiGeneratorDto> list, Map<String, ApiComponent> components) throws JsonProcessingException {
    OpenAPI openAPI = new OpenAPI(SpecVersion.V30);
    Info info = new Info();
    info.setTitle("api gen by js");
    info.setDescription("test");
    info.version("0.0.1");
    openAPI.info(info);
    //ObjectMapper yamlMapper = Yaml.mapper();

    //String result = yamlMapper.writerWithDefaultPrettyPrinter().forType(OpenAPI.class).writeValueAsString(openAPI);
    Paths pathMap = new Paths();
    Map<String, Schema> schemas = new HashMap<>();
    //Map<String, List<ApiGeneratorDto>> paths = list.stream().collect(Collectors.groupingBy(o -> o.getServiceName() + "_" + o.getPath()));


    Set<String> c = components.values().stream()
        .flatMap(o -> findString(o.getData()).stream())
        .collect(Collectors.toSet());

    for (ApiGeneratorDto item : list) {
      Operation operation = deserialOperation(item);
      Server server = new Server();
      server.url(item.getUrl());
      operation.addServersItem(server);
      appendPath(pathMap, item, operation);

      c.addAll(getParameterRef(operation));
      c.addAll(getRequestBody(operation));
      c.addAll(getResponseRef(operation));


      for(String name: c){
        ApiComponent apiComponent = components.get(name);
        if(apiComponent != null){
          Schema schema = deserialSchema(apiComponent.getData());
          schema.additionalProperties(null);
          schemas.put(name, schema);
        }
      }
    }

    openAPI.setPaths(pathMap);

    Components component = new Components();
    component.setSchemas(schemas);
    openAPI.setComponents(component);

    return mapper.writerWithDefaultPrettyPrinter().forType(OpenAPI.class).writeValueAsString(openAPI);
  }

  public Set<String> findString(String value){
    Set<String> results = new HashSet<>();
    int s = value.indexOf(PREFIX_SCHEMA);

    while(s > -1){
      value = value.substring(s);
      int e = value.indexOf("'");
      String substring = value.substring(PREFIX_SCHEMA.length(), e);
      results.add(substring);
      value = value.substring(e);
      s = value.indexOf(PREFIX_SCHEMA);
    }
    return results;

  }

  private Set<String> getRequestBody(Operation operation) {
    if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null) {
      return operation.getRequestBody().getContent().values()
          .stream().filter(o -> Objects.nonNull(o.getSchema()))
          .map(o -> o.getSchema().get$ref().replace(PREFIX_SCHEMA, ""))
          .collect(Collectors.toSet());
    }
    return new HashSet<>();
  }

  private Set<String> getResponseRef(Operation operation) {
    if (operation.getResponses() == null
        || !operation.getResponses().containsKey("200")
        || operation.getResponses().get("200").getContent() == null) {
      return new HashSet<>();
    }

    return operation.getResponses().get("200")
        .getContent().values().stream()
        .filter(o -> Objects.nonNull(o.getSchema().get$ref()))
        .map(o -> o.getSchema().get$ref().replace(PREFIX_SCHEMA, ""))
        .collect(Collectors.toSet());

  }

  private Set<String> getParameterRef(Operation operation) {
    if (operation.getParameters() == null) {
      return new HashSet<>();
    }
    return operation.getParameters().stream()
        .filter(o -> Objects.nonNull(o.getSchema()) && Objects.nonNull(o.getSchema().get$ref()))
        .map(o -> {
          return o.getSchema().get$ref().replace(PREFIX_SCHEMA, "");
        })
        .collect(Collectors.toSet());
  }

  private Operation deserialOperation(ApiGeneratorDto item) {
    try {
      return mapper.readValue(item.getData(), Operation.class);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  private Schema deserialSchema(String data) {
    try {

      return mapper.readValue(data, Schema.class);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  private Paths appendPath(Paths pathMap, ApiGeneratorDto item, Operation operation) {
    PathItem pi = pathMap.getOrDefault(item.getPath(), new PathItem());

    if (item.getMethod() == HttpMethodEnum.GET) {
      pi.setGet(operation);
    } else if (item.getMethod() == HttpMethodEnum.POST) {
      pi.setPost(operation);
    } else if (item.getMethod() == HttpMethodEnum.PUT) {
      pi.setPut(operation);
    } else if (item.getMethod() == HttpMethodEnum.OPTION) {
      pi.setOptions(operation);
    } else if (item.getMethod() == HttpMethodEnum.HEAD) {
      pi.setHead(operation);
    } else if (item.getMethod() == HttpMethodEnum.PATCH) {
      pi.setPatch(operation);
    } else if (item.getMethod() == HttpMethodEnum.DELETE) {
      pi.setDelete(operation);
    } else if (item.getMethod() == HttpMethodEnum.TRACE) {
      pi.setTrace(operation);
    }

    pathMap.put(item.getPath(), pi);
    return pathMap;
  }

}
