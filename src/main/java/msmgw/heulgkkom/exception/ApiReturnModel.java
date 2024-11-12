package msmgw.heulgkkom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiReturnModel<T> extends ResponseEntity<ApiReturnModel.ResponseBody<T>> {

    public static ApiReturnModel<Void> OK() {
        return new ApiReturnModel<>(HttpStatus.OK.name(), null, null);
    }


    public static <T> ApiReturnModel<T> OK(T data) {
        return new ApiReturnModel<>(HttpStatus.OK.name(), null, data);
    }

    public static ApiReturnModel<Void> FailChangeHttpStatus(HttpStatus httpStatusCode, String code, String message) {
        return new ApiReturnModel<>(httpStatusCode, code, message, null);
    }

    public static ApiReturnModel<Void> Fail(String code, String message) {
        return new ApiReturnModel<>(code, message, null);
    }

    public ApiReturnModel(HttpStatus httpStatusCode, String code, String message, T data) {
        super(new ApiReturnModel.ResponseBody<>(code, message, data), httpStatusCode);
    }

    public ApiReturnModel(String code, String message, T data) {
        this(HttpStatus.OK, code, message, data);
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseBody<T> {
        private String code;
        private String message;
        private T data;
    }
}
