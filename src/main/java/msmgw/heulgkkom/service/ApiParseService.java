package msmgw.heulgkkom.service;


import static msmgw.heulgkkom.model.constant.HttpMethodEnum.DELETE;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.GET;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.HEAD;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.OPTION;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PATCH;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.POST;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PUT;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.TRACE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.MyApi;
import msmgw.heulgkkom.entity.MyComponent;
import msmgw.heulgkkom.model.ApiDto;
import msmgw.heulgkkom.service.parse.OpenApiSpecParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiParseService {

    private final OpenApiSpecParser openApiSpecParser;

    private final static ObjectMapper mapper = Yaml.mapper();


    @Transactional
    public SwaggerParseResult parseUri(String location) {
        return new OpenAPIV3Parser().readLocation(location, null, null);
    }

    @Transactional
    public void parseSpec(String data, Long projectId, String versionId) {

        OpenAPI openAPI = new OpenAPIV3Parser().readContents(data).getOpenAPI();

        List<MyApi> myApis = extractPaths(openAPI, versionId, projectId);

        List<MyComponent> myComponents = extractComponents(openAPI, projectId, versionId);

    }

    private List<MyApi> extractPaths(OpenAPI openAPI, String versionId, Long projectId) {

        Set<Entry<String, PathItem>> entries = openAPI.getPaths().entrySet();

        return extractedPathInfo(openAPI, entries)
                .map(o ->
                        MyApi.builder()
                                .projectId(projectId)
                                .path(o.getPath())
                                .method(o.getMethod())
                                .specBody(convertObjectToString(o.getOp()))
                                .parameter(o.getParameter())
                                .request(o.getRequestBody())
                                .response(o.getResponse())
                                .version(versionId)
                                .build()
                )
                .toList();
    }

    private Stream<ApiDto> extractedPathInfo(OpenAPI openAPI, Set<Entry<String, PathItem>> entries) {
        return entries.stream()
                .flatMap(e -> {
                    String k = e.getKey();
                    PathItem v = e.getValue();
                    List<ApiDto> group = new ArrayList<>();

                    group.add(openApiSpecParser.printOperation(openAPI, v.getHead(), HEAD, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getGet(), GET, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getPost(), POST, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getDelete(), DELETE, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getPut(), PUT, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getOptions(), OPTION, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getPatch(), PATCH, k));
                    group.add(openApiSpecParser.printOperation(openAPI, v.getTrace(), TRACE, k));

                    return group.stream();
                })
                .filter(Objects::nonNull);
    }

    private List<MyComponent> extractComponents(OpenAPI openAPI, Long projectId, String versionId) {
        if (Objects.isNull(openAPI.getComponents().getSchemas())) {
            return List.of();
        }

        return openAPI.getComponents().getSchemas().entrySet().stream()
                .map(o -> MyComponent.builder()
                        .name(o.getKey())
                        .projectId(projectId)
                        .specBody(convertObjectToString(o.getValue()))
                        .version(versionId)
                        .build()
                ).toList();
    }

    private static String convertObjectToString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
