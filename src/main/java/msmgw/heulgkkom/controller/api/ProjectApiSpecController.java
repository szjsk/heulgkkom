package msmgw.heulgkkom.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.service.ApiGeneratorService;
import msmgw.heulgkkom.service.ProjectApiSpecService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/spec")
@RequiredArgsConstructor
@Slf4j
public class ProjectApiSpecController {

    private final ApiGeneratorService apiGeneratorService;

    @GetMapping("/api-json/{projectName}")
    public Map<String, String> requestApi(@PathVariable String projectName, @RequestParam(value = "envType", required = false) String envType) {

        return apiGeneratorService.makeSpec(projectName, envType);
    }

}