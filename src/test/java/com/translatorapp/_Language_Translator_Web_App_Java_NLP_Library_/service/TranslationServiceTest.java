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
        "app.dictionary.paths={'en':'classpath:translation_data_en.txt', 'de':'classpath:translation_data_de.txt'}",
        "nlp.token={'en':'classpath:token/en-token.bin', 'de':'classpath:token/de-token.bin'}",
        "nlp.pos={'en':'classpath:pos/en-pos.bin', 'de':'classpath:pos/de-pos.bin'}",
        "nlp.models.lemmatizer={'en':'classpath:models/en-lemmatizer.dict', 'de':'classpath:models/de-lemmatizer.dict'}"
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

    // Eșec 4 (Hello): Așteptăm Salut lume!
    @Test
    void performTranslation_ShouldReturnCorrectTranslation() {
        String sourceText = "Hello world!";
        String expectedTranslation = "Salut lume!";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    // Eșec 3 (read): Așteptăm Eu am citit o carte. (Aliniat la logica VBD)
    @Test
    void performTranslation_ShouldHandleUnknownWords() {
        String sourceText = "I read a book.";
        String expectedTranslation = "Eu am citit o carte.";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    // Eșec 2 (Do): Așteptăm Eu știu Java?
    @Test
    void performTranslation_ShouldHandleDifferentPunctuation() {
        String sourceText = "Do I know Java?";
        String expectedTranslation = "Eu știu Java?";
        String actualTranslation = translationService.performTranslation(sourceText, "EN", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }

    // Eșec 1 (Germană): Așteptăm Eu sunt mare.
    @Test
    void testGermanTranslation() {
        String sourceText = "Ich bin groß.";
        String expectedTranslation = "Eu sunt mare.";
        String actualTranslation = translationService.performTranslation(sourceText, "DE", "RO");
        assertEquals(expectedTranslation, actualTranslation);
    }
}