package nl.aoros.learning.techreads.webapp.client;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author adrian oros
 */
public class AccountsApiClient {
    private final WebClient webClient;

    public AccountsApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
