package nl.aoros.learning.techreads.webapp.client;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author adrian oros
 */
public class SearchApiClient {
    private final WebClient webClient;

    public SearchApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
