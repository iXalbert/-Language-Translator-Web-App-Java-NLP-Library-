package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.postag.POSTaggerME;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {

    @InjectMocks
    private TranslationService translationService;

    @Mock
    private TranslationDictionary mockDictonary;
    @Mock
    private TokenizerME mockTokenizer;
    @Mock
    private POSTaggerME mockPosTagger;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(translationService, "tokenizer", mockTokenizer);
        ReflectionTestUtils.setField(translationService, "posTagger", mockPosTagger);

    }

    @Test
    void testSimpleSentenceTranslation(){

        String sourceText = "Hello World!";

        String[] tokens = {"Hello", "world", "!"};
        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"NNP", "NN", "."};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        when(mockDictonary.getTranslation("hello", "NNP")).thenReturn("Salut");
        when(mockDictonary.getTranslation("world", "NN")).thenReturn("lume");
        when(mockDictonary.getTranslation("!", ".")).thenReturn("!");

        String result = translationService.performTranslation(sourceText, "EN", "RO");

        assertEquals("Salut lume!", result, "Traducerea ar trebui sa includa majuscule si punctuatie corecta.");


    }

    @Test
    void testEmptySourceText(){

        String result = translationService.performTranslation("", "EN", "RO");

        assertEquals("Te rog să introduci text pentru traducere.", result, "Ar trebui sa returneze mesajul de eroare pentru text gol.");
    }

    @Test
    void testUntranslatedWordFallBack(){

        String sourceText = "I love you.";

        String[] tokens = {"I", "love", "you", "."};
        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"NNP", "VBP", "NNP", "."};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        when(mockDictonary.getTranslation("i", "NNP")).thenReturn(null);
        when(mockDictonary.getTranslation("love", "VBP")).thenReturn(null);
        when(mockDictonary.getTranslation("you", "NNP")).thenReturn("you");
        when(mockDictonary.getTranslation(".", ".")).thenReturn(".");

        String result = translationService.performTranslation(sourceText, "EN", "RO");

        assertEquals("I love you.", result, "Cuvintele netraduse ar terbui sa ramana in limba sursa.");
    }
}
