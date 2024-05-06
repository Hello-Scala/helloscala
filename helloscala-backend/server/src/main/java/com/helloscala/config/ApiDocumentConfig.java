package com.helloscala.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentConfig {

    @Bean
    public OpenAPI springOpenApi() {
        Contact contact = new Contact()
            .name("HelloScala")
            .url("http:://www.helloscala.com")
            .email("helloscala@outlook.com");

        Info info = new Info()
            .title("HelloScala API Documents")
            .description("HelloScala API Documents")
            .contact(contact)
            .version("v1.1.0");

        return new OpenAPI().info(info);

    }
}
