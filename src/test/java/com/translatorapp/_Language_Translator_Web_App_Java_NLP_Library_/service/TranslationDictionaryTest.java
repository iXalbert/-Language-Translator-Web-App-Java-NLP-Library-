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
@TestPropertySource(locations = "classpath:application-test.properties")
public class TranslationDictionaryTest {

    @Autowired
    private TranslationDictionary dictionary;

    @Test
    void testDictionaryIsInitialized() {
        assertNotNull(dictionary);
    }

    @Test
    void testExistingWordTranslation() {
        assertEquals("salut", dictionary.getTranslation("hello", "NNP"));
    }

    @Test
    void testNonExistingWordReturnsNull() {
        assertNull(dictionary.getTranslation("inexistent", "NN"));
    }

    @Test
    void testPOSDependentTranslation() {
        assertEquals("salut", dictionary.getTranslation("hello", "NNP"));
        assertNull(dictionary.getTranslation("hello", "VBD"));
    }

    @Test
    void testJavaTranslation() {
        assertEquals("java", dictionary.getTranslation("java", "NNP"));
    }

    @Test
    void testPunctuationTranslation() {
        assertEquals("!", dictionary.getTranslation("!", "."));
    }
}