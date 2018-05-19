package nl.aoros.learning.techreads.account.endpoint;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author adrian oros
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
}
