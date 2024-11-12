package msmgw.heulgkkom.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.PermittedProjectApiEntity;
import msmgw.heulgkkom.entity.ProjectEntity;
import msmgw.heulgkkom.entity.ProjectApiEntity;
import msmgw.heulgkkom.entity.ProjectSpecEntity;
import msmgw.heulgkkom.model.api.ApiDTO;
import msmgw.heulgkkom.model.api.ApprovalRequestApiDTO;
import msmgw.heulgkkom.model.api.RequestApiDTO;
import msmgw.heulgkkom.model.constant.PermittedStatus;
import msmgw.heulgkkom.repository.PermittedProjectApiRepository;
import msmgw.heulgkkom.repository.ProjectApiRepository;
import msmgw.heulgkkom.repository.ProjectRepository;
import msmgw.heulgkkom.repository.ProjectSpecRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static msmgw.heulgkkom.exception.ServiceExceptionCode.DATA_NOT_FOUND;
import static msmgw.heulgkkom.exception.ServiceExceptionCode.HAS_REQUEST_OR_APPROVED_API;
import static msmgw.heulgkkom.model.constant.PermittedStatus.APPROVED;
import static msmgw.heulgkkom.model.constant.PermittedStatus.REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestApiService {
    private final ProjectRepository projectRepository;
    private final ProjectApiRepository projectApiRepository;
    private final ProjectSpecRepository projectSpecRepository;
    private final PermittedProjectApiRepository permittedProjectApiRepository;

    @Transactional
    public void requestApiUsage(Long reqProjectSeq, RequestApiDTO target, String loginUserName) {

        ProjectEntity myProjectEntity = projectRepository.findById(target.getTargetProjectSeq())
                .orElseThrow(() -> DATA_NOT_FOUND.toException(" target.getTargetProjectSeq : " + target.getTargetProjectSeq()));

        projectApiRepository
                .findByProject_ProjectSeqAndVersionIdAndPathAndMethod(myProjectEntity.getProjectSeq(), myProjectEntity.getVersionId(), target.getTargetPath(), target.getTargetMethod())
                .orElseThrow(() -> DATA_NOT_FOUND.toException(" seq : " + myProjectEntity.getProjectSeq() + ", version : " + myProjectEntity.getVersionId() + ", path : " + target.getTargetPath() + ", method : " + target.getTargetMethod()));

        PermittedProjectApiEntity permittedEntity = permittedProjectApiRepository
                .findByReqProject_ProjectSeqAndTargetPathAndTargetMethod(target.getTargetProjectSeq(), target.getTargetPath(), target.getTargetMethod())
                .map(o -> {
                    if (o.getStatus().equals(REQUEST) || o.getStatus().equals(APPROVED)) {
                        throw HAS_REQUEST_OR_APPROVED_API.toException();
                    }
                    o.setStatus(REQUEST);
                    o.setRequestReason(target.getRequestReason());
                    o.setRequestedBy(loginUserName);
                    o.setRequestAt(LocalDateTime.now());
                    o.setRequestContact(target.getRequestContact());

                    return o;
                })
                .orElseGet(() ->
                        PermittedProjectApiEntity.builder()
                                .reqProject(projectRepository.findById(reqProjectSeq).orElseThrow())
                                .targetProject(myProjectEntity)
                                .targetPath(target.getTargetPath())
                                .targetMethod(target.getTargetMethod())
                                .status(REQUEST)
                                .requestReason(target.getRequestReason())
                                .requestedBy(loginUserName)
                                .requestAt(LocalDateTime.now())
                                .requestContact(target.getRequestContact())
                                .build()
                );

        permittedProjectApiRepository.save(permittedEntity);
    }


    public void approvalApiUsage(Long projectSeq, ApprovalRequestApiDTO approvalApiDTO, String loginUserName) {

        List<PermittedProjectApiEntity> entities = approvalApiDTO.getPermittedProjectApiSeqs().stream()
                .map(seq ->
                        permittedProjectApiRepository.findById(seq)
                                .map(o -> {
                                    o.setStatus(approvalApiDTO.getStatus());
                                    o.setApprovalReason(approvalApiDTO.getApprovalReason());
                                    o.setApprovalContact(approvalApiDTO.getApprovalContact());
                                    o.setApprovalAt(LocalDateTime.now());
                                    o.setApprovalContact(loginUserName);
                                    return o;
                                })
                                .orElseThrow(() -> DATA_NOT_FOUND.toException(" projectSeq : " + projectSeq + " approvalApiDTO : " + approvalApiDTO))
                )
                .toList();

        permittedProjectApiRepository.saveAll(entities);
    }

    public Page<ApiDTO> retrieveApis(String envType, String projectName, String apiName, Pageable pageable) {
        Page<ProjectApiEntity> projectsWithApis = projectApiRepository.findProjectsWithApis(projectName, envType, apiName, pageable);
        return projectsWithApis.map(o -> ApiDTO.builder()
                .projectId(o.getProject().getProjectSeq())
                .projectName(o.getProject().getProjectName())
                .envType(o.getProject().getEnvType())
                .domainUrl(o.getProject().getDomainUrl())
                .assignMail(o.getProject().getAssignMail())
                .versionId(o.getVersionId())
                .apiId(o.getApiSeq())
                .path(o.getPath())
                .method(o.getMethod())
                .parameter(o.getParameter())
                .request(o.getRequest())
                .response(o.getResponse())
                .build()
        );
    }

    public List<String> retrieveVersions(Long projectSeq) {
        return projectSpecRepository.findAllByProject_ProjectSeq(projectSeq)
                .stream()
                .map(ProjectSpecEntity::getVersionId)
                .toList();
    }

}