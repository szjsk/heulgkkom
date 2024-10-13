package msmgw.heulgkkom.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.model.api.ProjectDTO;
import msmgw.heulgkkom.service.MyApiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static msmgw.heulgkkom.config.WrapResponseCode.SPEC_PARSE_EXCEPTION;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class MyApiManagerController {

  private final MyApiService myApiService;

  @PostMapping("/{projectId}/{versionId}")
  public void createApiVersion(@RequestParam("file") MultipartFile file, @PathVariable Long projectId, @PathVariable String versionId) {

      String content = null;
      try {
          content = new String(file.getBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
          throw SPEC_PARSE_EXCEPTION.toException(e);
      }

      myApiService.createApis(content, projectId, versionId);
  }

    @PutMapping("/update/{projectId}")
    public void updateProject(@PathVariable Long projectId, @RequestBody ProjectDTO param) {
        myApiService.updateProject(projectId, param);
    }

    @PutMapping("/update/{projectId}/{versionId}")
    public void updateProjectVersion(@PathVariable Long projectId, @PathVariable String versionId) {
        myApiService.updateProjectVersion(projectId, versionId);
    }

    @PostMapping("")
    public void createProject(@RequestBody ProjectDTO param) {
        myApiService.createProject(param);
    }
}