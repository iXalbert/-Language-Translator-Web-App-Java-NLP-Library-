package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TranslationDictionary.class)
@TestPropertySource(properties = {

        "app.dictionary.paths={'en':'classpath:translation_data_en.txt', 'de':'classpath:translation_data_de.txt'}",
        "nlp.token={'en':'classpath:token/en-token.bin', 'de':'classpath:token/de-token.bin'}",
        "nlp.pos={'en':'classpath:pos/en-pos.bin', 'de':'classpath:pos/de-pos.bin'}",
        "nlp.models.lemmatizer={'en':'classpath:models/en-lemmatizer.dict', 'de':'classpath:models/de-lemmatizer.dict'}"
})
public class TranslationDictionaryTest {

    @Autowired
    private TranslationDictionary dictionary;

    @Test
    void testDictionaryIsInitialized() {
        assertNotNull(dictionary);
    }

    @Test
    void testExistingWordTranslation() {
        assertEquals("salut", dictionary.getTranslation("hello", "NNP", "en"));
    }

    @Test
    void testNonExistingWordReturnsNull() {
        assertNull(dictionary.getTranslation("inexistent", "NN", "en"));
    }

    @Test
    void testPOSDependentTranslation() {
        assertEquals("salut", dictionary.getTranslation("hello", "NNP", "en"));
        assertNull(dictionary.getTranslation("hello", "VBD", "en"));
    }

    @Test
    void testJavaTranslation() {
        assertEquals("java", dictionary.getTranslation("java", "NNP", "en"));
    }

    @Test
    void testPunctuationTranslation() {
        assertEquals("!", dictionary.getTranslation("!", ".", "en"));
    }

    @Test
    void testGermanWordTranslation() {
        assertEquals("casă", dictionary.getTranslation("haus", "NN", "de"));
    }

    @Test
    void testGermanVerbTranslation() {
        assertEquals("a merge", dictionary.getTranslation("gehen", "VBP", "de"));
    }
}