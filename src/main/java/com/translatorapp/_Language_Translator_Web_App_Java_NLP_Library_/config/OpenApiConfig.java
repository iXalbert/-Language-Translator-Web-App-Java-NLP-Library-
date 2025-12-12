package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NLP Java Translator API")
                        .version("1.0.0")
                        .description("API-ul de traducere lingvistică bazat pe OpenNLP. Suportă traducerea din Engleză (EN) și Germană (DE) în Română (RO).")
                        .contact(new Contact()
                                .name("Albert Rosca")
                                .email("roscaalbert123@gmail.com")) // Adaugă-ți datele de contact
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}