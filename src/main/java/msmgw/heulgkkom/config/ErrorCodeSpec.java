package msmgw.heulgkkom.config;

public interface ErrorCodeSpec {
    String getCode();

    String getMessage();

    String getPrintMessage();

    default RuntimeException toException(String messageArgs) {
        return new RuntimeException(this.getPrintMessage() + " " + messageArgs);
    }

    default String getMessage(String locale) {
        throw new UnsupportedOperationException("please implements getMessage(String locale) method");
    }
}
