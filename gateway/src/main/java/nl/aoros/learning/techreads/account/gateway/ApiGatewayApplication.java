package nl.aoros.learning.techreads.account.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    @Value("${accounts.uri}")
    private String accountsUrl;
    @Value("${books.uri}")
    private String bookUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts", r -> r.path("/accounts/**")
                        .filters(f ->
                                f.addRequestHeader("X-Account-Id", "id")
                                        .rewritePath("/accounts/(?<apiPath>.*)", "/$\\{apiPath}"))
                        .uri(accountsUrl)
                )
                .route("books", r -> r.path("/books/**")
                        .filters(f ->
                                f.addRequestHeader("X-Account-Id", "id")
                                        .rewritePath("/books/(?<apiPath>.*)", "/$\\{apiPath}")
                        ).uri(bookUrl)
                )
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}