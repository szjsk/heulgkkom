package msmgw.heulgkkom.config;

public interface ErrorCodeSpec {
    String getCode();

    String getMessage();

    String getPrintMessage();

    default RuntimeException toException() {
        return new RuntimeException(this.getPrintMessage());
    }

    RuntimeException toException(String messageArgs);

    default RuntimeException toException(Throwable cause) {
        return new RuntimeException(this.getPrintMessage(), cause);
    }

    default RuntimeException toException(String messageArgs, Throwable cause) {
        return new RuntimeException(this.getPrintMessage() + " " + messageArgs, cause);
    }

    default String getMessage(String locale) {
        throw new UnsupportedOperationException("please implements getMessage(String locale) method");
    }
}
