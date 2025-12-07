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
        "app.dictionary.path=classpath:translation_data_en.txt",
        "nlp.token={en:classpath:token/en-token.bin}",
        "nlp.pos={en:classpath:pos/en-pos.bin}",
        "nlp.models.lemmatizer={en:classpath:models/en-lemmatizer.dict}"
})
public class TranslationDictionaryTest {

    @Autowired
    private TranslationDictionary dictionary;

    @Test
    void testDictionaryIsInitialized() {
        // Dacă dicționarul se încarcă fără excepții și este injectat, este OK
        assertNotNull(dictionary);
    }

    @Test
    void testExistingWordTranslation() {
        // Presupunând că "hello,NNP,Salut" este în fișierul de dicționar
        assertEquals("Salut", dictionary.getTranslation("hello", "NNP"));
    }

    @Test
    void testNonExistingWordReturnsNull() {
        assertNull(dictionary.getTranslation("inexistent", "NN"));
    }

    @Test
    void testPOSDependentTranslation() {
        // Acest test necesită două intrări pentru același cuvânt cu POS-uri diferite
        // Presupunând că fișierul conține: "read,VBP,citesc" și "read,NN,lectură"
        // (Deoarece nu am fișierul, acest test e speculativ)
        // Dacă fișierul e simplu, ne bazăm pe:
        assertEquals("Salut", dictionary.getTranslation("hello", "NNP"));
        assertNull(dictionary.getTranslation("hello", "VBD")); // Ar trebui să eșueze dacă POS-ul nu se potrivește
    }

    @Test
    void testJavaTranslation() {
        // Presupunând că "java,NNP,Java" este în dicționar (ca nume propriu)
        assertEquals("Java", dictionary.getTranslation("java", "NNP"));
    }

    @Test
    void testPunctuationTranslation() {
        // Presupunând că punctuația nu este tradusă sau nu e în dicționar
        assertNull(dictionary.getTranslation("!", "."));
    }
}