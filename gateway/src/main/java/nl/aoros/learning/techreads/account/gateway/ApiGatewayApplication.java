package nl.aoros.learning.techreads.account.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@SpringBootApplication
public class ApiGatewayApplication {
    @Value("${accounts.api.url}")
    private String accountsApiUrl;
    @Value("${books.api.url}")
    private String booksApiUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts", r -> r.path("/accounts/**")
                        .filters(f ->
                                f.addRequestHeader("X-Account-Id", "id")
                                        .rewritePath("/accounts/(?<apiPath>.*)", "/$\\{apiPath}"))
                        .uri(accountsApiUrl)
                )
                .route("books", r -> r.path("/books/**")
                        .filters(f ->
                                f.addRequestHeader("X-Account-Id", "id")
                                        .rewritePath("/books/(?<apiPath>.*)", "/$\\{apiPath}")
                        ).uri(booksApiUrl)
                )
                .build();
    }

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurerComposite() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
