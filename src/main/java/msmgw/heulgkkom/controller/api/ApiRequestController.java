package msmgw.heulgkkom.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.model.api.ProjectDTO;
import msmgw.heulgkkom.model.api.RequestApiDTO;
import msmgw.heulgkkom.service.MyApiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static msmgw.heulgkkom.config.WrapResponseCode.SPEC_PARSE_EXCEPTION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiRequestController {

    private final MyApiService myApiService;

    @PostMapping("/req/{projectId}")
    public void requestApi(@PathVariable Long projectId, @RequestBody RequestApiDTO requestApiDTO) {

    }

    @PostMapping("/res/{projectId}")
    public void responseApi(@PathVariable Long projectId, @RequestBody RequestApiDTO requestApiDTO) {

    }
}