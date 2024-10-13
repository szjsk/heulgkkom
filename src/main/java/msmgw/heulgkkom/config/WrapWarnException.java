package msmgw.heulgkkom.config;

import lombok.Getter;

@Getter
public class WrapWarnException extends RuntimeException {

    public WrapWarnException(ErrorCodeSpec type, String messageArg, Throwable cause) {
        super(type.getPrintMessage() + ( messageArg == null ? "" : " " + messageArg), cause);
    }

}
