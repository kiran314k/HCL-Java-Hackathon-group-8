package com.hcl.payments.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
     OpenAPI paymentsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payments Service API")
                        .description("API documentation for the Payments microservice")
                        .version("v1.0"));
    }
}
