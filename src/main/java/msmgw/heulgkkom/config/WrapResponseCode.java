package msmgw.heulgkkom.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WrapResponseCode implements ErrorCodeSpec {
    WRONG_PARAMETER("0000", "wrong parameter", true),
    ;

    private final String code;
    private final String message;
    private final Boolean isWarn;

    @Override
    public String getPrintMessage() {
        return this.getCode() + " " + this.getMessage();
    }

    @Override
    public RuntimeException toException(String messageArgs) {
        if (this.getIsWarn()) {
            return new WrapWarnException(this, messageArgs);
        }
        return new WrapErrorException(this, messageArgs);
    }
}
