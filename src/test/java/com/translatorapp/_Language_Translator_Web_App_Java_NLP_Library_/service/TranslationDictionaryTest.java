package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TranslationDictionaryTest {

    private TranslationDictionary dictionary;

    @BeforeEach
    public void setUp(){
        dictionary = new TranslationDictionary();
        dictionary.init();
    }

    @Test // TEST 1: Traducere existentă
    void testExistingWordTranslation(){
        String translated = dictionary.getTranslation("world", "hello");
        assertEquals("salut", translated, "Traducerea pentru 'hello' ar trebui să fie 'salut'.");
    }

    @Test // TEST 2: Punctuație existentă
    void testPunctuationTranslation(){
        String translated = dictionary.getTranslation("world", "!");
        assertEquals("!", translated, "Punctuația ar trebui să se returneze corect.");
    }

    @Test // TEST 3: Cuvânt inexistent (ar trebui să returneze null)
    void testNonExistingWordReturnsNull() {
        String translated = dictionary.getTranslation("world", "hzdvgbli");
        assertNull(translated, "Ar trebui să returneze NULL pentru un cuvânt ce nu există.");
    }

    @Test // TEST 4: Verifică inițializarea dicționarului
    void testDictionaryIsInitialized() {
        String translated = dictionary.getTranslation("world", "world");
        assertNotNull(translated, "Dicționarul ar trebui să fie inițializat și să conțină 'world'.");
    }

    @Test // TEST 5: Verifică traducerea 'java'
    void testJavaTranslation(){
        String translated = dictionary.getTranslation("world", "java");
        assertEquals("java", translated, "Traducerea pentru 'java' ar trebui să fie 'java'.");
    }
}