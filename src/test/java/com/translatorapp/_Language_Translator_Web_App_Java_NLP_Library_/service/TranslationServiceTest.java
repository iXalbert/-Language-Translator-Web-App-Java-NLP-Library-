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
@TestPropertySource(properties = {
        "app.dictionary.path=classpath:translation_data_en.txt",
        "nlp.token={en:classpath:token/en-token.bin}",
        "nlp.pos={en:classpath:pos/en-pos.bin}",
        "nlp.models.lemmatizer={en:classpath:models/en-lemmatizer.dict}"
})
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
        String expectedTranslation = "Salut lume!";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void performTranslation_ShouldHandleUnknownWords() {
        String sourceText = "I read a book.";
        String expectedTranslation = "I read a book.";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void performTranslation_ShouldHandleDifferentPunctuation() {
        String sourceText = "Do I know Java?";
        String expectedTranslation = "Do I know Java?";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }
}