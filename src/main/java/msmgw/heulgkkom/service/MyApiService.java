package msmgw.heulgkkom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ProjectApiEntity;
import msmgw.heulgkkom.entity.ProjectEntity;
import msmgw.heulgkkom.entity.ProjectSpecEntity;
import msmgw.heulgkkom.model.api.ProjectDTO;
import msmgw.heulgkkom.model.api.ProjectSearchDTO;
import msmgw.heulgkkom.model.parse.SpecApiDto;
import msmgw.heulgkkom.repository.ProjectApiRepository;
import msmgw.heulgkkom.repository.ProjectRepository;
import msmgw.heulgkkom.repository.ProjectSpecRepository;
import msmgw.heulgkkom.service.parse.OpenApiSpecParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static msmgw.heulgkkom.exception.ServiceExceptionCode.CONFLICT_PROJECT_NAME;
import static msmgw.heulgkkom.exception.ServiceExceptionCode.DATA_NOT_FOUND;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyApiService {

    private final OpenApiSpecParser openApiSpecParser;
    private final ProjectRepository projectRepository;
    private final ProjectSpecRepository projectSpecRepository;
    private final ProjectApiRepository projectApiRepository;
    private final static ObjectMapper mapper = Yaml.mapper();

    @Transactional
    public void updateProjectVersion(Long projectSeq, String version) {
        projectSpecRepository.findById(version)
                .filter(o -> o.getProject().getProjectSeq().equals(projectSeq))
                .orElseThrow(() -> DATA_NOT_FOUND.toException("projectSeq :" + projectSeq + ", version : " + version));

        ProjectEntity entity = projectRepository.findById(projectSeq)
                .orElseThrow(() -> DATA_NOT_FOUND.toException("projectSeq :" + projectSeq));

        entity.setVersionId(version);
        entity.setModifiedAt(LocalDateTime.now());
        projectRepository.save(entity);

    }

    @Transactional
    public void updateProject(Long id, ProjectDTO projectDTO) {
        ProjectEntity entity = projectRepository.findById(id).orElseThrow(() -> DATA_NOT_FOUND.toException("updateProject id :" + id));

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

        projectRepository.save(entity);

    }

    @Transactional
    public ProjectEntity createProject(ProjectDTO projectDTO) {
        projectRepository.findByProjectNameAndEnvType(projectDTO.getProjectName(), projectDTO.getEnvType())
                .ifPresent(p -> {
                    throw CONFLICT_PROJECT_NAME.toException();
                });

        ProjectEntity entity = ProjectEntity.builder()
                .projectName(projectDTO.getProjectName())
                .envType(projectDTO.getEnvType())
                .domainUrl(projectDTO.getDomainUrl())
                .assignMail(projectDTO.getAssignMail())
                .versionId(null)
                .createdAt(LocalDateTime.now())
                .createdBy(projectDTO.getAssignMail())
                .build();

        return projectRepository.save(entity);
    }

    @Transactional
    public void createApis(String data, Long projectSeq) {

        OpenAPI openAPI = new OpenAPIV3Parser().readContents(data).getOpenAPI();
        ProjectEntity projectEntity = projectRepository.findById(projectSeq).orElseThrow();

        String versionId = projectEntity.getProjectName() + "_" + System.currentTimeMillis();

        //insert project apis
        List<ProjectApiEntity> projectApiEntities = extractPaths(openAPI, projectEntity, versionId);
        projectApiRepository.saveAll(projectApiEntities);

        //insert project spec
        projectSpecRepository.save(ProjectSpecEntity.builder()
                .versionId(versionId)
                .project(projectEntity)
                .specBody(data)
                .build()
        );

    }

    public List<ProjectSearchDTO> retrieveProject(String projectName) {
        return projectRepository.findAllByProjectName(projectName).stream()
                .map(o->
                    ProjectSearchDTO.builder()
                            .projectSeq(o.getProjectSeq())
                            .projectName(o.getProjectName())
                            .versionId(o.getVersionId())
                            .envType(o.getEnvType())
                            .domainUrl(o.getDomainUrl())
                            .assignMail(o.getAssignMail())
                            .build()
                )
                .toList();

    }

    private List<ProjectApiEntity> extractPaths(OpenAPI openAPI, ProjectEntity projectEntity, String versionId) {

        Set<Entry<String, PathItem>> entries = openAPI.getPaths().entrySet();

        return extractedPathInfo(openAPI, entries)
                .map(o ->
                        ProjectApiEntity.builder()
                                .project(projectEntity)
                                .versionId(versionId)
                                .path(o.getPath())
                                .method(o.getMethod())
                                .parameter(o.getParameter())
                                .request(o.getRequestBody())
                                .response(o.getResponse())
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

    private static String convertObjectToString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
