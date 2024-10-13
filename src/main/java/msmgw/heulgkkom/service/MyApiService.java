package msmgw.heulgkkom.service;


import static msmgw.heulgkkom.config.WrapResponseCode.CONFLICT_PROJECT_NAME;
import static msmgw.heulgkkom.config.WrapResponseCode.DATA_NOT_FOUND;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import msmgw.heulgkkom.entity.Project;
import msmgw.heulgkkom.model.api.ProjectDTO;
import msmgw.heulgkkom.model.parse.SpecApiDto;
import msmgw.heulgkkom.repository.MyApiRepository;
import msmgw.heulgkkom.repository.MyComponentRepository;
import msmgw.heulgkkom.repository.MyProjectRepository;
import msmgw.heulgkkom.service.parse.OpenApiSpecParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyApiService {

    private final OpenApiSpecParser openApiSpecParser;
    private final MyProjectRepository myProjectRepository;
    private final MyApiRepository myApiRepository;
    private final MyComponentRepository myComponentRepository;
    private final static ObjectMapper mapper = Yaml.mapper();

    @Transactional
    public void updateProjectVersion(Long id, String version) {
        Project entity = myProjectRepository.findById(id).orElseThrow(() -> DATA_NOT_FOUND.toException("updateProjectVersion id :" + id));
        entity.setVersion(version);
        entity.setModifiedAt(LocalDateTime.now());
        myProjectRepository.save(entity);

    }

    @Transactional
    public void updateProject(Long id, ProjectDTO projectDTO) {
        Project entity = myProjectRepository.findById(id).orElseThrow(() -> DATA_NOT_FOUND.toException("updateProject id :" + id));

        if (Objects.nonNull(projectDTO.getProjectName())) {
            entity.setProjectName(projectDTO.getProjectName());
        }
        if (Objects.nonNull(projectDTO.getEnvType())) {
            entity.setEnvType(projectDTO.getEnvType());
        }
        if (Objects.nonNull(projectDTO.getDomainUrl())) {
            entity.setDomainUrl(projectDTO.getDomainUrl());
        }
        if (Objects.nonNull(projectDTO.getAssignMail())) {
            entity.setAssignMail(projectDTO.getAssignMail());
        }

        entity.setModifiedAt(LocalDateTime.now());

        myProjectRepository.save(entity);

    }

    @Transactional
    public void createProject(ProjectDTO projectDTO) {
        myProjectRepository.findByProjectNameAndEnvType(projectDTO.getProjectName(), projectDTO.getEnvType())
                .ifPresent(p -> {
                    throw CONFLICT_PROJECT_NAME.toException();
                });

        Project entity = Project.builder()
                .projectName(projectDTO.getProjectName())
                .envType(projectDTO.getEnvType())
                .domainUrl(projectDTO.getDomainUrl())
                .assignMail(projectDTO.getAssignMail())
                .version(null)
                .createdAt(LocalDateTime.now())
                .createdBy(projectDTO.getAssignMail())
                .build();

        myProjectRepository.save(entity);

    }

    @Transactional
    public void createApis(String data, Long projectId, String versionId) {

        OpenAPI openAPI = new OpenAPIV3Parser().readContents(data).getOpenAPI();

        List<MyApi> myApis = extractPaths(openAPI, versionId, projectId);
        myApiRepository.saveAll(myApis);
        List<MyComponent> myComponents = extractComponents(openAPI, projectId, versionId);
        myComponentRepository.saveAll(myComponents);
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

    private Stream<SpecApiDto> extractedPathInfo(OpenAPI openAPI, Set<Entry<String, PathItem>> entries) {
        return entries.stream()
                .flatMap(e -> {
                    String k = e.getKey();
                    PathItem v = e.getValue();
                    List<SpecApiDto> group = new ArrayList<>();

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
