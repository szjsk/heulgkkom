package msmgw.heulgkkom.controller;

import static java.util.Objects.isNull;
import static msmgw.heulgkkom.model.constant.AllowStatusEnum.CANCEL;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.AllowApiListDto;
import msmgw.heulgkkom.model.AllowRequestDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import msmgw.heulgkkom.model.DomainGroupDto;
import msmgw.heulgkkom.model.DomainVersionDto;
import msmgw.heulgkkom.service.ApiAllowService;
import msmgw.heulgkkom.service.ApiDomainService;
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
@RequestMapping("/api-request")
@RequiredArgsConstructor
@Slf4j
public class ApiRequestController {

  private final ApiManagerService apiManagerService;
  private final ApiAllowService apiAllowService;
  private final ApiDomainService apiDomainService;


  @GetMapping("/path/{domainId}")
  public List<ApiPathDto> retrieveApiPath(@PathVariable Long domainId){
    return apiManagerService.retrieveApiPathByDefault(domainId);
  }

  @GetMapping("/allow/{pathId}")
  public List<AllowApiListDto> retrieveAllowApis(@PathVariable Long pathId){
    return apiAllowService.findAllowApiLists(pathId);
  }

  @GetMapping("/allow-by-domain/{domainId}")
  public List<AllowApiListDto> retrieveAllowApisByDomainId(@PathVariable Long domainId){
    return apiAllowService.findAllowApiListByDomain(domainId);
  }


  @GetMapping("/other-domain/{domainId}")
  public List<DomainGroupDto> retrieveSameDomainGroup(@PathVariable Long domainId){
    return apiDomainService.retrieveSameDomainGroup(domainId);
  }

  @PostMapping("/allow-request")
  public boolean requestAllowApi(@RequestBody AllowRequestDto param){

    ControllerUtil.assertRequest(()-> isNull(param.getDomainId())).onThrow("domain id can not be null");
    ControllerUtil.assertRequest(()-> isNull(param.getPathId())).onThrow("path id can not be null");
    ControllerUtil.assertRequest(()-> isNull(param.getReqReason())).onThrow("reason can not be null");
    ControllerUtil.assertRequest(()-> isNull(param.getRequestedContact())).onThrow("contact can not be null");

    ControllerUtil.assertRequest(()-> isNull(param.getAllowId()) && param.getStatus() == CANCEL)
        .onThrow("contact can not be null");

    return apiAllowService.requestAllowApi(param);
  }


}