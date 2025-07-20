package com.proj.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                // .route(p -> p
                //         .path("/get")
                //         .filters(f -> f
                //                 .addRequestHeader("MyHeader", "MyURI")
                //                 .addRequestParameter("Param", "MyValue"))
                //         .uri("http://httpbin.org:80"))
                .route(p -> p.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route(p -> p.path("/account/**")
                        .uri("lb://account-service"))
                .route(p -> p.path("/customer/**")
                        .uri("lb://customer-service"))
                .route(p -> p.path("/transaction/**")
                        .uri("lb://transaction-service"))
                .route(p -> p.path("/ledger/**")
                        .uri("lb://ledger-service"))
                .route(p -> p.path("/notification/**")
                        .uri("lb://notification-service"))
                .build();
     }
    }
