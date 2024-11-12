package msmgw.heulgkkom.exception;

import lombok.Getter;

@Getter
public class ServiceRuntimeException extends RuntimeException {

    private final ErrorCodeSpec errorCode;
    private final Object[] messageArgs;

    public ServiceRuntimeException(ErrorCodeSpec errorCode) {
        super(errorCode.getMessageWithCode());
        this.errorCode = errorCode;
        this.messageArgs = null;
    }

    public ServiceRuntimeException(ErrorCodeSpec errorCode, String... messageArgs) {
        super(errorCode.getMessageWithCode() + String.join(", ", messageArgs));
        this.errorCode = errorCode;
        this.messageArgs = messageArgs;
    }

    public ServiceRuntimeException(ErrorCodeSpec errorCode, Throwable cause) {
        super(errorCode.getMessageWithCode(), cause);
        this.errorCode = errorCode;
        this.messageArgs = null;
    }

    public ServiceRuntimeException(ErrorCodeSpec errorCode, Throwable cause, String... messageArgs) {
        super(errorCode.getMessageWithCode() + String.join(", ", messageArgs), cause);
        this.errorCode = errorCode;
        this.messageArgs = messageArgs;
    }
}
