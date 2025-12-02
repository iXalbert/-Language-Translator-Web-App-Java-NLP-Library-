package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TranslationDictionaryTest {

    private TranslationDictionary dictionary;

    @BeforeEach
    public void setup(){
        dictionary = new TranslationDictionary();
        dictionary.init();
    }

    @Test
    void testExistingWordTranslation(){
        String translated = dictionary.getTranslation("hello", null);
        assertEquals("salut", translated, "Traducerea pentru 'hello' ar trebui să fie 'salut'.");
    }

    @Test
    void testPunctuationTranslation(){
        String translated = dictionary.getTranslation("!", null);
        assertEquals("!", translated, "Punctuația ar trebui să se returneze corect.");
    }

    @Test
    void testNonExistingWordReturnsNull(){
        String translated = dictionary.getTranslation("nonexistentword", null);
        assertNull(translated, "Ar trebui să returneze NULL pentru un cuvânt ce nu există.");
    }

    @Test
    void testDictionaryIsInitialized(){
        String translated = dictionary.getTranslation("world", null);
        assertNotNull(translated, "Dicționarul ar trebui să fie inițializat și să conțină 'world'.");
    }

    @Test
    void testJavaTranslation(){
        String translated = dictionary.getTranslation("java", null);
        assertEquals("java", translated, "Traducerea pentru 'java' ar trebui să fie 'java'.");
    }
}