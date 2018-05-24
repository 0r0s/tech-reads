package nl.aoros.learning.techreads.book.service.exception;

/**
 * @author adrian oros
 */
public class NotFoundException extends RuntimeException {
    private final String errorKey;
    public NotFoundException(String message) {
        super(message);
        this.errorKey = null;
    }

    public NotFoundException(String message, String errorKey) {
        super(message);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
