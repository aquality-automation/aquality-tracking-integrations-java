package aquality.tracking.integrations.core;

public class AqualityException extends Exception {

    public AqualityException(String message) {
        super(message);
    }

    public AqualityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
