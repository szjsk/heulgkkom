package msmgw.heulgkkom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.PermittedProjectApiEntity;
import msmgw.heulgkkom.entity.ProjectEntity;
import msmgw.heulgkkom.entity.ProjectSpecEntity;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.model.constant.PermittedStatus;
import msmgw.heulgkkom.repository.PermittedProjectApiRepository;
import msmgw.heulgkkom.repository.ProjectRepository;
import msmgw.heulgkkom.repository.ProjectSpecRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static msmgw.heulgkkom.exception.ServiceExceptionCode.DATA_NOT_FOUND;
import static msmgw.heulgkkom.exception.ServiceExceptionCode.SPEC_CREATE_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGeneratorService {

    private final ProjectRepository projectRepository;
    private final ProjectSpecRepository projectSpecRepository;
    private final PermittedProjectApiRepository permittedProjectApiRepository;

    public Map<String, String> makeSpec(String projectName, String envType) {
        //프로젝트 존재여부 체크.
        ProjectEntity projectEntity = projectRepository.findByProjectNameAndEnvType(projectName, envType)
                .orElseThrow(() -> DATA_NOT_FOUND.toException("proejct not found : " + projectName + " , " + envType));

        //내 프로젝트를 기준으로 승인된 프로젝트 조회
        return permittedProjectApiRepository.findAllByReqProject_ProjectSeqAndStatus(projectEntity.getProjectSeq(), PermittedStatus.APPROVED).stream()
                .collect(Collectors.groupingBy(PermittedProjectApiEntity::getTargetProject))
                .entrySet().stream()
                .map((e) -> {
                    ProjectSpecEntity spec = projectSpecRepository.findByProject_ProjectSeqAndVersionId(e.getKey().getProjectSeq(), e.getKey().getVersionId())
                            .orElseThrow(() -> DATA_NOT_FOUND.toException("spec not found : " + projectName + " , " + envType));

                    //open api에서는 한개의 Path에 여러 Method가 포함된 형태이므로
                    // 코드 작업을 용이하게 하기 위해 허용된 경로를 기준으로 메소드리스트 추출
                    Map<String, List<HttpMethodEnum>> pathWithMethod = e.getValue().stream()
                            .collect(Collectors.groupingBy(
                                    PermittedProjectApiEntity::getTargetPath,
                                    Collectors.mapping(PermittedProjectApiEntity::getTargetMethod, Collectors.toList())
                            ));
                    //사용할 프로젝트에서 허용되지 않은 API는 제거 한다. 전송된 specBody를 기준으로 제거한다.
                    //todo 제거가 아닌 조합버전이 나을 수도 있음. 추후 논의하여 변경.
                    String openApiSpec = removeNotUseApi(e.getKey().getDomainUrl(), spec.getSpecBody(), pathWithMethod);
                    return Map.entry(e.getKey().getProjectName(), openApiSpec);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String removeNotUseApi(String domainUri, String specBody, Map<String, List<HttpMethodEnum>> pathWithMethod) {

        OpenAPI openAPI = new OpenAPIV3Parser().readContents(specBody).getOpenAPI();

        openAPI.setServers(List.of(new Server().url(domainUri)));
        Paths paths = new Paths();

        openAPI.getPaths().entrySet().stream()
                .filter(e -> pathWithMethod.containsKey(e.getKey()))
                .forEach(e -> {
                    e.getValue().setServers(null);
                    paths.addPathItem(e.getKey(), removeNotUseMethod(e.getValue(), pathWithMethod.get(e.getKey())));
                });

        openAPI.setPaths(paths);

        //허용된 path에서 사용된 schema만 추출하여 사용할 schema만 남긴다.
        // schema내에서 자식 schema로 사용되는 경우가 있으므로 해당 schema도 추출하여 사용할 schema만 남긴다.
        Set<String> schemaRefsForPath = findAllByUseComponentRefs(openAPI.getComponents().getSchemas(), findAllByUseSchemaRefs(paths));

        openAPI.getComponents().getSchemas().entrySet().removeIf(e -> !schemaRefsForPath.contains(e.getKey()));

        try {
            return Json.mapper()
                    .writerWithDefaultPrettyPrinter()
                    .forType(OpenAPI.class)
                    .writeValueAsString(openAPI);
        } catch (JsonProcessingException e) {
            throw SPEC_CREATE_EXCEPTION.toException("domainUri : " + domainUri, e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Set<String> findAllByUseComponentRefs(Map<String, Schema> components, Set<String> schemaRefsForPath) {
        Set<String> allRefs = new HashSet<>(schemaRefsForPath);
        Set<String> componentsRefs = schemaRefsForPath.stream()
                .flatMap(ref -> extractAllRefs(components.get(ref)).stream())
                .collect(Collectors.toSet());

        allRefs.addAll(componentsRefs);
        return allRefs;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Set<String> extractAllRefs(Schema schema) {
        Set<String> refs = new HashSet<>();
        if (Objects.isNull(schema)) {
            return refs;
        }

        if (Objects.nonNull(schema.get$ref())) {
            refs.add(replaceRefs(schema.get$ref()));
        }

        if (Objects.nonNull(schema.getProperties())) {
            schema.getProperties().values().forEach(property -> refs.addAll(extractAllRefs((Schema<?>) property)));
        }

        if (Objects.nonNull(schema.getItems())) {
            refs.addAll(extractAllRefs(schema.getItems()));
        }

        return refs;
    }

    private Set<String> findAllByUseSchemaRefs(Paths paths) {
        return paths.values().stream()
                .flatMap(p -> p.readOperations().stream())
                .flatMap(o -> {
                    Set<String> refs = new HashSet<>();
                    refs.addAll(extractRequestBody(o.getRequestBody()));
                    refs.addAll(extractResponseRef(o.getResponses()));
                    refs.addAll(extractParameterRef(o.getParameters()));
                    return refs.stream().filter(StringUtils::isNotBlank);
                })
                .collect(Collectors.toSet());
    }

    private Set<String> extractParameterRef(List<Parameter> parameters) {
        if (Objects.isNull(parameters)) {
            return new HashSet<>();
        }
        return parameters.stream()
                .filter(o -> Objects.nonNull(o.getSchema()) && Objects.nonNull(o.getSchema().get$ref()))
                .flatMap(o -> Stream.of(replaceRefs(o.get$ref()), replaceRefs(o.getSchema().get$ref())))
                .collect(Collectors.toSet());
    }

    private Set<String> extractRequestBody(RequestBody requestBody) {

        if (Objects.isNull(requestBody) || Objects.isNull(requestBody.getContent())) {
            return new HashSet<>();
        }

        HashSet<String> refs = requestBody.getContent().values().stream()
                .filter(o -> Objects.nonNull(o.getSchema()))
                .map(mediaType ->
                        replaceRefs(mediaType.getSchema().get$ref())
                )
                .collect(Collectors.toCollection(HashSet::new));

        refs.add(replaceRefs(requestBody.get$ref()));

        return refs;

    }

    private Set<String> extractResponseRef(ApiResponses responses) {
        if (Objects.isNull(responses)) {
            return new HashSet<>();
        }

        return responses.values().stream()
                .flatMap(res -> {
                    HashSet<String> refs = res.getContent().values().stream()
                            .filter(content -> Objects.nonNull(content.getSchema().get$ref()))
                            .map(content -> replaceRefs(content.getSchema().get$ref()))
                            .collect(Collectors.toCollection(HashSet::new));

                    refs.add(replaceRefs(res.get$ref()));
                    return refs.stream();
                }).collect(Collectors.toSet());
    }

    private String replaceRefs(String refs) {
        if (Objects.isNull(refs)) {
            return StringUtils.EMPTY;
        }
        return refs.replace(Components.COMPONENTS_SCHEMAS_REF, StringUtils.EMPTY);
    }


    private PathItem removeNotUseMethod(PathItem pathItem, List<HttpMethodEnum> methods) {
        if (!methods.contains(HttpMethodEnum.GET)) {
            pathItem.setGet(null);
        }
        if (!methods.contains(HttpMethodEnum.POST)) {
            pathItem.setPost(null);
        }
        if (!methods.contains(HttpMethodEnum.PUT)) {
            pathItem.setPut(null);
        }
        if (!methods.contains(HttpMethodEnum.OPTION)) {
            pathItem.setOptions(null);
        }
        if (!methods.contains(HttpMethodEnum.HEAD)) {
            pathItem.setHead(null);
        }
        if (!methods.contains(HttpMethodEnum.PATCH)) {
            pathItem.setPatch(null);
        }
        if (!methods.contains(HttpMethodEnum.DELETE)) {
            pathItem.setDelete(null);
        }
        if (!methods.contains(HttpMethodEnum.TRACE)) {
            pathItem.setTrace(null);
        }
        return pathItem;
    }
/*
    private static OpenAPI createOpenAPIInfo() {
        OpenAPI openAPI = new OpenAPI(SpecVersion.V30);
        Info info = new Info();
        info.setTitle("api generator ");
        info.setDescription("test");
        info.version("0.0.1");
        openAPI.info(info);
        return openAPI;
    }

    public Set<String> findString(String value) {
        Set<String> results = new HashSet<>();
        int s = value.indexOf(PREFIX_SCHEMA);

        while (s > -1) {
            value = value.substring(s);
            int e = value.indexOf("'");
            String substring = value.substring(PREFIX_SCHEMA.length(), e);
            results.add(substring);
            value = value.substring(e);
            s = value.indexOf(PREFIX_SCHEMA);
        }
        return results;

    }

    private Set<String> extractRequestBody(RequestBody requestBody) {
        if (requestBody != null && requestBody.getContent() != null) {
            return requestBody.getContent().values()
                    .stream().filter(o -> Objects.nonNull(o.getSchema()))
                    .map(o -> o.getSchema().get$ref().replace(PREFIX_SCHEMA, ""))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private Set<String> extractResponseRef(ApiResponses responses) {
        if (responses == null
                || !responses.containsKey("200")
                || responses.get("200").getContent() == null) {
            return new HashSet<>();
        }

        return operation.getResponses().get("200")
                .getContent().values().stream()
                .filter(o -> Objects.nonNull(o.getSchema().get$ref()))
                .map(o -> o.getSchema().get$ref().replace(PREFIX_SCHEMA, ""))
                .collect(Collectors.toSet());

    }

    private Set<String> extractParameterRef(List<Parameter> parameters) {
        if (parameters == null) {
            return new HashSet<>();
        }
        return parameters.stream()
                .filter(o -> Objects.nonNull(o.getSchema()) && Objects.nonNull(o.getSchema().get$ref()))
                .map(o -> {
                    return o.getSchema().get$ref().replace(PREFIX_SCHEMA, "");
                })
                .collect(Collectors.toSet());
    }

    private Operation deserialOperation(String specBody) {
        try {
            return mapper.readValue(specBody, Operation.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Schema deserializeSchema(String data) {
        try {

            return mapper.readValue(data, Schema.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Paths appendPath(Paths pathMap, ProjectApiEntity item, Operation operation) {
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
    }*/

}
