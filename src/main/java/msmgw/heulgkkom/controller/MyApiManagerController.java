package msmgw.heulgkkom.controller;

import static java.util.Objects.isNull;
import static msmgw.heulgkkom.model.constant.AllowStatusEnum.CANCEL;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.AllowRequestDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.DomainVersionDto;
import msmgw.heulgkkom.service.ApiAllowService;
import msmgw.heulgkkom.service.ApiManagerService;
import msmgw.heulgkkom.service.DomainVersionService;
import msmgw.heulgkkom.util.ControllerUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/my-api")
@RequiredArgsConstructor
@Slf4j
public class MyApiManagerController {

  private final ApiManagerService apiManagerService;
  private final DomainVersionService domainVersionService;
  private final ApiAllowService apiAllowService;

  @PostMapping("/{serviceId}")
  public Long createApiVersion(@RequestParam("file") MultipartFile file, @PathVariable Long serviceId) throws IOException {

    String content = new String(file.getBytes(), StandardCharsets.UTF_8);

    return apiManagerService.parseString(content, serviceId);
  }

  @PostMapping("/domain-version")
  public DomainVersion mappingDomainApiVersion(@RequestBody DomainVersionDto param) throws IOException {
    return domainVersionService.upsertService(param);
  }

  @GetMapping("/{serviceId}")
  public List<ApiManagerVersionDto> getApiVersionDomainList(@PathVariable Long serviceId) throws IOException {

    return apiManagerService.retrieveApiVersionDomainList(serviceId);
  }

  @PostMapping("/{domainId}/{versionId}")
  public boolean mappingVersion(@PathVariable Long domainId, @PathVariable Long versionId) throws IOException {

    apiManagerService.domainVersionRepository(domainId, versionId);

    return true;
  }

  @GetMapping("/path/{versionId}")
  public List<ApiPath> retrieveApiPath(@PathVariable Long versionId){
    return apiManagerService.retrieveApiPathList(versionId);
  }


  @PostMapping("/approval")
  public boolean requestAllowApi(@RequestBody AllowRequestDto param){

    ControllerUtil.assertRequest(()-> isNull(param.getStatus())).onThrow("reason can not be null");
    ControllerUtil.assertRequest(()-> isNull(param.getAllowId())).onThrow("allow Id can not be null");

    return apiAllowService.approvalAllowApi(param);
  }
}