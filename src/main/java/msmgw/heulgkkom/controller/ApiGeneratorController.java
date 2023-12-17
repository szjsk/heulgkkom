package msmgw.heulgkkom.controller;

import static msmgw.heulgkkom.util.ControllerUtil.assertRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.model.DomainManagerDto;
import msmgw.heulgkkom.service.ApiDomainService;
import msmgw.heulgkkom.service.ApiGeneratorService;
import msmgw.heulgkkom.service.ApiServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generator")
@RequiredArgsConstructor
@Slf4j
public class ApiGeneratorController {

  private final ApiGeneratorService apiGeneratorService;

  @GetMapping("/gradle-file/{serviceName}")
  public String getGradle(@PathVariable String serviceName, @RequestParam(defaultValue = "dev") String group) {
    return apiGeneratorService.extractGradleTextBy(serviceName, group);
  }


  @GetMapping("/spec/{domainId}")
  public String getSpec(@PathVariable String domainId) throws JsonProcessingException {
    return apiGeneratorService.makeSpec(Long.valueOf(domainId));
  }

}