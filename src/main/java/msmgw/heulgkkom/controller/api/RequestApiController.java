package msmgw.heulgkkom.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msmgw.heulgkkom.exception.ApiReturnModel;
import msmgw.heulgkkom.model.api.ApiDTO;
import msmgw.heulgkkom.model.api.ApprovalRequestApiDTO;
import msmgw.heulgkkom.model.api.RequestApiDTO;
import msmgw.heulgkkom.service.RequestApiService;
import msmgw.heulgkkom.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RequestApiController {

    private final RequestApiService requestApiService;
    private final UserService userService;

    /**
     * api 사용 요청.
     *
     * @param reqProjectSeq Api를 사용할 project Id
     * @param targetDto     사용할 project, API Id와 요청 사유, 연락처
     * @param userDetails   로그인 된 사용자 정보
     */
    @PostMapping("/request/{reqProjectSeq}")
    public ApiReturnModel<Void> requestApi(@PathVariable Long reqProjectSeq, @RequestBody RequestApiDTO targetDto, @AuthenticationPrincipal UserDetails userDetails) {
        userService.verifyProjectAuthority(userDetails, reqProjectSeq);

        requestApiService.requestApiUsage(reqProjectSeq, targetDto, userDetails.getUsername());

        return ApiReturnModel.OK();
    }

    /**
     * api 사용 승인.
     *
     * @param targetProjectSeq Api를 승인할 project Id
     * @param approvalApiDTO   승인여부 및 승인 할 API Id와 사유
     * @param userDetails      로그인 된 사용자 정보
     */
    @PostMapping("/approval/{targetProjectSeq}")
    public ApiReturnModel<Void> approvalApi(@PathVariable Long targetProjectSeq, @RequestBody ApprovalRequestApiDTO approvalApiDTO, @AuthenticationPrincipal UserDetails userDetails) {
        userService.verifyProjectAuthority(userDetails, targetProjectSeq);

        requestApiService.approvalApiUsage(targetProjectSeq, approvalApiDTO, userDetails.getUsername());

        return ApiReturnModel.OK();
    }

    /**
     * 모든 API 목록 조회.
     *
     * @param envType     환경 타입 (dev, prod)
     * @param projectName 프로젝트 이름 (like 검색)
     * @param apiName     API 이름 (like 검색)
     * @param pageable    페이징 정보
     * @return API 목록
     */
    @GetMapping("/list/{envType}")
    public ApiReturnModel<Page<ApiDTO>> responseApi(@PathVariable String envType,
                                                    @RequestParam(required = false) String projectName, @RequestParam(required = false) String apiName,
                                                    Pageable pageable) {

        return ApiReturnModel.OK(requestApiService.retrieveApis(envType, projectName, apiName, pageable));
    }

    /**
     * Project의 버전 조회
     *
     */
    @GetMapping("/versions/{projectSeq}")
    public ApiReturnModel<List<String>> responseApi(@PathVariable Long projectSeq) {
        return ApiReturnModel.OK(requestApiService.retrieveVersions(projectSeq));
    }
}