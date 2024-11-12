package msmgw.heulgkkom.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ProjectEntity;
import msmgw.heulgkkom.exception.ApiReturnModel;
import msmgw.heulgkkom.model.api.ProjectDTO;
import msmgw.heulgkkom.model.api.ProjectSearchDTO;
import msmgw.heulgkkom.service.MyApiService;
import msmgw.heulgkkom.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static msmgw.heulgkkom.exception.ServiceExceptionCode.SPEC_PARSE_EXCEPTION;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectManagerController {

  private final MyApiService myApiService;
  private final UserService userService;

    /**
     * open api swagger json/yml 파일 입력
     *
     * @param file 업로드된 swagger 파일 경로
     * @param projectSeq 저장된 project pk
     */
  @PostMapping("/{projectSeq}")
  public ApiReturnModel<Void> createApiVersion(@RequestParam("file") MultipartFile file, @PathVariable Long projectSeq) {

      String content = null;
      try {
          content = new String(file.getBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
          throw SPEC_PARSE_EXCEPTION.toException(e);
      }

      myApiService.createApis(content, projectSeq);
      return ApiReturnModel.OK();
  }

    @PutMapping("/update/{projectSeq}")
    public ApiReturnModel<Void> updateProject(@PathVariable Long projectSeq, @RequestBody ProjectDTO param) {
        myApiService.updateProject(projectSeq, param);
        return ApiReturnModel.OK();
    }

    /**
     * 프로젝트 버전 업데이트
     */
    @PutMapping("/update/{projectSeq}/{versionId}")
    public ApiReturnModel<Void> updateProjectVersion(@PathVariable Long projectSeq, @PathVariable String versionId) {
        myApiService.updateProjectVersion(projectSeq, versionId);
        return ApiReturnModel.OK();
    }

    /**
     * 프로젝트 생성
     */
    @PostMapping("")
    public ApiReturnModel<Long> createProject(@RequestBody ProjectDTO param) {
        ProjectEntity project = myApiService.createProject(param);
        userService.updateProjectToUser(project.getProjectName());
        return ApiReturnModel.OK(project.getProjectSeq());
    }

    /**
     * 프로젝트 조회
     */
    @GetMapping("")
    public ApiReturnModel<List<ProjectSearchDTO>> retrieveProject(@RequestParam String projectName) {
        return ApiReturnModel.OK(myApiService.retrieveProject(projectName));
    }
}