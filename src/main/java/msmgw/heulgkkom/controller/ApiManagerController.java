package msmgw.heulgkkom.controller;

import static msmgw.heulgkkom.util.ControllerUtil.assertRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.ApiVersion;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.ApiVersionDto;
import msmgw.heulgkkom.model.DomainManagerDto;
import msmgw.heulgkkom.model.DomainVersionDto;
import msmgw.heulgkkom.service.ApiManagerService;
import msmgw.heulgkkom.service.DomainVersionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiManagerController {

  private final ApiManagerService apiManagerService;
  private final DomainVersionService domainVersionService;

  @PostMapping("/{serviceId}")
  public Long createApiVersion(@RequestParam("file") MultipartFile file, @PathVariable long serviceId) throws IOException {

    String content = new String(file.getBytes(), StandardCharsets.UTF_8);

    return apiManagerService.parseString(content, serviceId);
  }

  @PostMapping("/domain-version")
  public DomainVersion mappingDomainApiVersion(@RequestBody DomainVersionDto param) throws IOException {
    return domainVersionService.upsertService(param);
  }

  @GetMapping("/{serviceId}")
  public List<ApiVersionDto> getApiVersionDomainList(@PathVariable long serviceId) throws IOException {

    return apiManagerService.retrieveApiVersionDomainList(serviceId);
  }

}