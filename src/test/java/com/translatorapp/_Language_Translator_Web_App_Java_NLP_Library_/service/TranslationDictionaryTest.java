package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.LanguageTranslatorWebAppJavaNlpLibraryApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LanguageTranslatorWebAppJavaNlpLibraryApplication.class)
//@ContextConfiguration(classes = {TranslationDictionary.class})
public class TranslationDictionaryTest {

    @Autowired
    private TranslationDictionary dictionary;

    @BeforeEach
    public void setup(){

    }

    @Test
    void testExistingWordTranslation(){
        String translated = dictionary.getTranslation("hello", "NNP");
        assertEquals("salut", translated, "Traducerea pentru 'hello' ar trebui să fie 'salut'.");
    }

    @Test
    void testPunctuationTranslation(){
        String translated = dictionary.getTranslation("!", ".");
        assertEquals("!", translated, "Punctuația ar trebui să se returneze corect.");
    }

    @Test
    void testNonExistingWordReturnsNull(){
        String translated = dictionary.getTranslation("nonexistentword", "NN");
        assertNull(translated, "Ar trebui să returneze NULL pentru un cuvânt ce nu există.");
    }

    @Test
    void testDictionaryIsInitialized(){
        String translated = dictionary.getTranslation("world", "NN");
        assertNotNull(translated, "Dicționarul ar trebui să fie inițializat și să conțină 'world'.");
    }

    @Test
    void testJavaTranslation(){
        String translated = dictionary.getTranslation("java", "NNP");
        assertEquals("java", translated, "Traducerea pentru 'java' ar trebui să fie 'java'.");
    }

    @Test
    void testPOSDependentTranslation(){
        String translated_noun = dictionary.getTranslation("run", "NN");
        String translated_verb = dictionary.getTranslation("run", "VBP");

        assertEquals("alergare", translated_noun, "Traducerea pentru 'run' in substantiv ar terbui sa fie 'alergare '");
        assertEquals("a alerga", translated_verb, "Traducerea pentru 'run' in verb ar trebui sa fie 'a alerga' ");
    }
}