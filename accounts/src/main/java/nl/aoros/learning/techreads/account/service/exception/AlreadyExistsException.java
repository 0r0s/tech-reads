package nl.aoros.learning.techreads.account.service.exception;

/**
 * @author adrian oros
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
