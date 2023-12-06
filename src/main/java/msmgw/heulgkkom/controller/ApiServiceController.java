package msmgw.heulgkkom.controller;

import static msmgw.heulgkkom.util.ControllerUtil.assertRequest;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiService;
import msmgw.heulgkkom.model.ServiceManagerDto;
import msmgw.heulgkkom.service.ApiServiceService;
import msmgw.heulgkkom.util.ControllerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
@Slf4j
public class ApiServiceController {

  private final ApiServiceService apiServiceService;

  @PostMapping
  public ApiService createService(ServiceManagerDto param) {
    assertRequest(()-> StringUtils.isBlank(param.getServiceName())).onThrow("service name is required.");
    assertRequest(()-> Objects.isNull(param.getStatus())).onThrow("service status  is required.");
    assertRequest(()-> StringUtils.isBlank(param.getServiceDesc())).onThrow("service description  is required.");
    return apiServiceService.insertService(param, "todo");
  }

  @PutMapping
  public ApiService updateService(ServiceManagerDto param) {
    assertRequest(()-> Objects.isNull(param.getServiceId())).onThrow("service id is required.");
    assertRequest(()-> StringUtils.isBlank(param.getServiceName())).onThrow("service name is required.");
    assertRequest(()-> Objects.isNull(param.getStatus())).onThrow("service status is required.");
    assertRequest(()-> StringUtils.isBlank(param.getServiceDesc())).onThrow("service description is required.");

    return apiServiceService.updateService(param, "todo");
  }


  @GetMapping("/{serviceId}")
  public ApiService getService(@PathVariable long serviceId) {
    return apiServiceService.getService(serviceId);
  }

  @GetMapping
  public List<ApiService> retrieveServices() {
    return apiServiceService.retrieveServices();
  }

  @GetMapping("/my-service")
  public List<ApiService> retrieveMyServices() {
    return apiServiceService.retrieveServices();
  }

}
