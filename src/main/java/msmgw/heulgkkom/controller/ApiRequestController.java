package msmgw.heulgkkom.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.AllowApiListDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import msmgw.heulgkkom.model.DomainVersionDto;
import msmgw.heulgkkom.service.ApiAllowService;
import msmgw.heulgkkom.service.ApiManagerService;
import msmgw.heulgkkom.service.DomainVersionService;
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


  @GetMapping("/path/{domainId}")
  public List<ApiPathDto> retrieveApiPath(@PathVariable Long domainId){
    return apiManagerService.retrieveApiPathByDefault(domainId);
  }

  @GetMapping("/allow/{pathId}")
  public List<AllowApiListDto> retrieveAllowApis(@PathVariable Long pathId){
    return apiAllowService.findAllowApiLists(pathId);
  }

}