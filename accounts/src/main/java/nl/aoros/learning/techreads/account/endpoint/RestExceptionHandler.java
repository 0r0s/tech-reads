package nl.aoros.learning.techreads.account.endpoint;

import lombok.extern.slf4j.Slf4j;
import nl.aoros.learning.techreads.account.service.exception.AlreadyExistsException;
import nl.aoros.learning.techreads.account.service.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author adrian oros
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {
    private static final String MESSAGE = "Mapping %s to API error response";
    private static final String ACCOUNT_NOT_FOUND_ERROR_KEY = "account.notfound";
    private static final String ACCOUNT_ALREADY_EXISTS_ERROR_KEY = "account.alreadyexists";
    private static final String VALIDATION_ERROR_KEY = "validation.error";

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.error(message, e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ACCOUNT_NOT_FOUND_ERROR_KEY, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAlreadyExists(AlreadyExistsException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.error(message, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ACCOUNT_ALREADY_EXISTS_ERROR_KEY, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.info(message, e);

        String fieldErrorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(VALIDATION_ERROR_KEY, fieldErrorMessage));
    }
}
