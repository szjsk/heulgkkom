package msmgw.heulgkkom.config;

import lombok.Getter;

@Getter
public class WrapErrorException extends RuntimeException {

    public WrapErrorException(ErrorCodeSpec type) {
        super(type.getPrintMessage());
    }

    public WrapErrorException(ErrorCodeSpec type, String messageArg) {
        super(type.getPrintMessage() + " " + messageArg);
    }

    public static WrapErrorException of(ErrorCodeSpec type) {
        return new WrapErrorException(type);
    }

}
