package nl.aoros.learning.techreads.account.endpoint;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author adrian oros
 */
@Data
@AllArgsConstructor
class RestApiResponse<T> {
    private T body;
    private String message;
    private String errorCode;

    private RestApiResponse() {}

    static<T> RestApiResponse<T> ok(T body) {
        return new RestApiResponse<>(body, null, null);
    }

    static<T> RestApiResponse<T> error(String errorKey, String message) {
        return new RestApiResponse<>(null, message, errorKey);
    }
}
