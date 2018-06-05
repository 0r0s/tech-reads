package nl.aoros.learning.techreads.webapp.client;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author adrian oros
 */
public class BooksApiClient {
    private final WebClient webClient;

    public BooksApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
