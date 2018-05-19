package nl.aoros.learning.techreads.account.service.exception;

/**
 * @author adrian oros
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
