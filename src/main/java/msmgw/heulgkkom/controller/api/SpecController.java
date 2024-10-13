package msmgw.heulgkkom.controller.api;

import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.model.api.RequestApiDTO;
import msmgw.heulgkkom.service.MyApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spec")
@RequiredArgsConstructor
@Slf4j
public class SpecController {

    private final MyApiService myApiService;

    @PostMapping("/generate/{projectName}/{envType}")
    public String requestApi(@PathVariable Long projectName, @PathVariable String envType) {

        return "";
    }

}