package msmgw.heulgkkom.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeSpec {
    String getCode();

    String getMessage();

    String getMessageWithCode();

    HttpStatus getHttpStatus();

    default RuntimeException toException() {
        return new RuntimeException(this.getMessageWithCode());
    }

    RuntimeException toException(String messageArgs);

    default RuntimeException toException(Throwable cause) {
        return new RuntimeException(this.getMessageWithCode(), cause);
    }

    default RuntimeException toException(String messageArgs, Throwable cause) {
        return new RuntimeException(this.getMessageWithCode() + " " + messageArgs, cause);
    }

    default String getMessage(String locale) {
        throw new UnsupportedOperationException("please implements getMessage(String locale) method");
    }
}
