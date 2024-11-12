package msmgw.heulgkkom.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // 옵션::커스텀 서비스 예외 처리
    @ExceptionHandler({ServiceRuntimeException.class})
    public ApiReturnModel<Void> handleCustomServiceException(ServiceRuntimeException e) {
        if (e.getErrorCode().getHttpStatus().is2xxSuccessful()) {
            log.warn("[{}]{} , {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage(), e.getMessage(), e);
            return ApiReturnModel.Fail(e.getErrorCode().getCode(), e.getMessage());
        } else {
            log.error("[{}]{} , {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage(), e.getMessage(), e);
            return ApiReturnModel.FailChangeHttpStatus(e.getErrorCode().getHttpStatus(), e.getErrorCode().getCode(), e.getMessage());
        }
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiReturnModel<Void> handleInternalErrorException(Exception e) {
        log.error("[Undefined Server Error]{}", e.getMessage(), e);
        return ApiReturnModel.FailChangeHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
    }


}
