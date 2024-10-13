package msmgw.heulgkkom.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WrapResponseCode implements ErrorCodeSpec {
    CONFLICT_PROJECT_NAME("CODE-1001", "project name must unique in envType.", true),
    SPEC_PARSE_EXCEPTION("CODE-1002", "please check spec file", true),
    DATA_NOT_FOUND("CODE-1003", "data not found", true),

    ;

    private final String code;
    private final String message;
    private final Boolean isWarn;

    @Override
    public String getPrintMessage() {
        return this.getCode() + " " + this.getMessage();
    }

    @Override
    public RuntimeException toException(String messageArgs, Throwable cause) {
        if (this.getIsWarn()) {
            return new WrapWarnException(this, messageArgs, cause);
        }
        return new WrapErrorException(this, messageArgs, cause);
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
