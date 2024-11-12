package msmgw.heulgkkom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ServiceExceptionCode implements ErrorCodeSpec {
    CONFLICT_PROJECT_NAME("CODE-1001", "project name must unique in envType.", HttpStatus.BAD_REQUEST),
    SPEC_PARSE_EXCEPTION("CODE-1002", "please check spec file", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("CODE-1003", "not found :: ", HttpStatus.INTERNAL_SERVER_ERROR),
    HAS_NOT_PROJECT_AUTH("CODE-1004", "has not project auth", HttpStatus.UNAUTHORIZED),
    HAS_REQUEST_OR_APPROVED_API("CODE-1005", "has request or approved api", HttpStatus.BAD_REQUEST),
    SPEC_CREATE_EXCEPTION("CODE-1006", "create spec exception", HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String getMessageWithCode() {
        return "[" + this.getCode() + "] " + this.getMessage();
    }

    @Override
    public RuntimeException toException(String messageArgs, Throwable cause) {
        return new ServiceRuntimeException(this, cause, messageArgs);
    }

    @Override
    public RuntimeException toException(String messageArgs) {
        return this.toException(messageArgs, null);

    }

    @Override
    public RuntimeException toException(Throwable cause) {
        return this.toException(null, cause);

    }

    @Override
    public RuntimeException toException() {
        return this.toException(null, null);
    }
}
