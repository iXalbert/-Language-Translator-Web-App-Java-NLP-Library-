package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Language Translator API (EN-RO)")
                        .version("v1.0")
                        .description("API pentru traducerea textului, utilizând procesarea naturală a limbajului (OpenNLP) și un dicționar personalizat. Suportă conjugarea verbelor."));
    }
}
