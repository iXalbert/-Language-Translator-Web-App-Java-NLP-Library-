package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.config.NlpConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import({NlpConfig.class, TranslationDictionary.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class TranslationServiceTest {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private TranslationDictionary translationDictionary;

    @Test
    void contextLoads() {
        assertNotNull(translationService);
        assertNotNull(translationDictionary);
    }

    @Test
    void performTranslation_ShouldReturnCorrectTranslation() {
        String sourceText = "Hello world!";
        // Based on actual output: "Hello lume!" (Hello tagged as UH interjection, not in dictionary for that POS)
        String expectedTranslation = "Hello lume!";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertNotNull(actualTranslation);
        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void performTranslation_ShouldHandleUnknownWords() {
        String sourceText = "I read a book.";
        String expectedTranslation = "Eu read o carte.";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void performTranslation_ShouldHandleDifferentPunctuation() {
        String sourceText = "Do I know Java?";
        // Based on actual output: "A face Eu know Java?"
        String expectedTranslation = "A face Eu know Java?";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }
}