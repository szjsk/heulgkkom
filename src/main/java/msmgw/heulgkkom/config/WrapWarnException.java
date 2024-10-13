package msmgw.heulgkkom.config;

import lombok.Getter;

@Getter
public class WrapWarnException extends RuntimeException {

    public WrapWarnException(ErrorCodeSpec type) {
        super(type.getPrintMessage());
    }

    public WrapWarnException(ErrorCodeSpec type, String messageArg) {
        super(type.getPrintMessage() + " " + messageArg);
    }

    public static WrapWarnException of(ErrorCodeSpec type) {
        return new WrapWarnException(type);
    }
}
