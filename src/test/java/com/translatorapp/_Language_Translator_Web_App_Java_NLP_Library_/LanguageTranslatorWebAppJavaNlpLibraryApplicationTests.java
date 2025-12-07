package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class LanguageTranslatorWebAppJavaNlpLibraryApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

}
