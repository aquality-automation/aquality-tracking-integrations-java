package aquality.tracking.integrations.core;

import java.io.IOException;

public class AqualityUncheckedException extends RuntimeException {

    public AqualityUncheckedException(String message, IOException cause) {
        super(message, cause);
    }

    public AqualityUncheckedException(String message, Exception cause) {
        super(message, cause);
    }

    public AqualityUncheckedException(String message) {
        super(message);
    }
}
