package msmgw.heulgkkom.config;

import lombok.Getter;

@Getter
public class WrapErrorException extends RuntimeException {

    public WrapErrorException(ErrorCodeSpec type, String messageArg, Throwable cause) {
        super(type.getPrintMessage() + ( messageArg == null ? "" : " " + messageArg), cause);
    }

}
