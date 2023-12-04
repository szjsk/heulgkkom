package msmgw.heulgkkom.controller;

import static msmgw.heulgkkom.util.ControllerUtil.assertRequest;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.DomainManagerDto;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.service.ApiDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/domain")
@RequiredArgsConstructor
@Slf4j
public class ApiDomainController {

  private final ApiDomainService apiDomainService;

  @PostMapping
  public ApiDomain createDomain(DomainManagerDto param) {
    assertRequest(()-> Objects.isNull(param.getServiceId())).onThrow("service id is required.");
    assertRequest(()-> StringUtils.isBlank(param.getUrl())).onThrow("url is required.");
    assertRequest(()-> StringUtils.isBlank(param.getGroup())).onThrow("group is required.");

    return apiDomainService.insertDomain(param, "todo");
  }

  @PutMapping
  public ApiDomain updateService(DomainManagerDto param) {
    assertRequest(()-> Objects.isNull(param.getDomainId())).onThrow("domain id is required.");
    assertRequest(()-> Objects.isNull(param.getServiceId())).onThrow("service id is required.");
    assertRequest(()-> StringUtils.isBlank(param.getUrl())).onThrow("url is required.");
    assertRequest(()-> StringUtils.isBlank(param.getGroup())).onThrow("group is required.");

    return apiDomainService.updateDomain(param, "todo");
  }

  @GetMapping("/{serviceId}")
  public List<ApiDomain> getService(@PathVariable long serviceId) {
    return apiDomainService.retrieveDomain(serviceId);
  }

}