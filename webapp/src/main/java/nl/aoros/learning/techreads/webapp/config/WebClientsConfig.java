package nl.aoros.learning.techreads.webapp.config;

import nl.aoros.learning.techreads.webapp.client.AccountsApiClient;
import nl.aoros.learning.techreads.webapp.client.BooksApiClient;
import nl.aoros.learning.techreads.webapp.client.SearchApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author adrian oros
 */
@Configuration
public class WebClientsConfig {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public @Bean AccountsApiClient accountsApiClient() {
        UriComponentsBuilder accountsApiUrl = UriComponentsBuilder.fromPath(apiGatewayUrl).path("/accounts");
        WebClient client = WebClient
                .builder()
                .baseUrl(accountsApiUrl.toUriString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return new AccountsApiClient(client);
    }

    public @Bean SearchApiClient searchApiClient() {
        UriComponentsBuilder searchApiUrl = UriComponentsBuilder.fromPath(apiGatewayUrl).path("/search");
        WebClient client = WebClient
                .builder()
                .baseUrl(searchApiUrl.toUriString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return new SearchApiClient(client);
    }

    public @Bean BooksApiClient booksApiClient() {
        UriComponentsBuilder searchApiUrl = UriComponentsBuilder.fromPath(apiGatewayUrl).path("/books");
        WebClient client = WebClient
                .builder()
                .baseUrl(searchApiUrl.toUriString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return new BooksApiClient(client);
    }

}
