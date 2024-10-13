package msmgw.heulgkkom.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.service.ApiParseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class MyApiManagerController {

  private final ApiParseService apiParseService;

  @PostMapping("/{projectId}/{versionId}")
  public void createApiVersion(@RequestParam("file") MultipartFile file, @PathVariable Long projectId, @PathVariable String versionId) {

      String content = null;
      try {
          content = new String(file.getBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

      apiParseService.parseSpec(content, projectId, versionId);
  }

}