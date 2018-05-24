package nl.aoros.learning.techreads.account.endpoint;

import lombok.extern.slf4j.Slf4j;
import nl.aoros.learning.techreads.account.service.exception.AlreadyExistsException;
import nl.aoros.learning.techreads.account.service.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestApiResponse> handleNotFound(NotFoundException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.error(message, e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RestApiResponse.error(ACCOUNT_NOT_FOUND_ERROR_KEY, e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<RestApiResponse> handleAlreadyExists(AlreadyExistsException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.error(message, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestApiResponse.error(ACCOUNT_ALREADY_EXISTS_ERROR_KEY, e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<RestApiResponse> handleValidationException(WebExchangeBindException e) {
        String message = String.format(MESSAGE, e.getClass().getName());
        log.info(message, e);

        String fieldErrorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestApiResponse.error(VALIDATION_ERROR_KEY, fieldErrorMessage));
    }
}
