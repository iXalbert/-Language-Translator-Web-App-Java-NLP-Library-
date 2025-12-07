package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.postag.POSTaggerME;

import opennlp.tools.tokenize.TokenizerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TranslationService.class, TranslationDictionary.class})
public class TranslationServiceTest {

    @Autowired
    private TranslationService translationService;

    @Mock
    private TranslationDictionary mockDictonary;
    @Mock
    private TokenizerME mockTokenizer;
    @Mock
    private POSTaggerME mockPosTagger;
    @Mock
    private DictionaryLemmatizer mockLemmatizer;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(translationService,"translationDictionary", mockDictonary);

        Map<String, TokenizerME> mockTokenizerMap = Map.of("en", mockTokenizer);
        Map<String, POSTaggerME> mockPosTaggerMap = Map.of("en", mockPosTagger);
        Map<String, DictionaryLemmatizer> mockLemmatizerMap = Map.of("en", mockLemmatizer);

        ReflectionTestUtils.setField(translationService, "tokenizerMap", mockTokenizerMap);
        ReflectionTestUtils.setField(translationService, "posTaggerMap", mockPosTaggerMap);
        ReflectionTestUtils.setField(translationService, "lemmatizerMap", mockLemmatizerMap);
    }

    @Test
    void testSimpleSentenceTranslation(){

        String sourceText = "Hello World!";

        String[] tokens = {"Hello", "world", "!"};
        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"NNP", "NN", "."};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        String[] lemmas = {"hello", "world", "O"};
        when(mockLemmatizer.lemmatize(eq(tokens), eq(tags))).thenReturn(lemmas);

        when(mockDictonary.getTranslation(eq("hello"), eq("NNP"))).thenReturn("Salut");
        when(mockDictonary.getTranslation(eq("world"), eq("NN"))).thenReturn("lume");
        //when(mockDictonary.getTranslation(eq("!"), eq("."))).thenReturn("!");

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

        String[] tags = {"PRP", "VBP", "PRP", "."};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);


        String[] lemmas = {"i", "love", "you", "O"};
        when(mockLemmatizer.lemmatize(eq(tokens), eq(tags))).thenReturn(lemmas);


        when(mockDictonary.getTranslation(eq("i"), eq("PRP"))).thenReturn(null);
        when(mockDictonary.getTranslation(eq("love"), eq("VBP"))).thenReturn(null);
        when(mockDictonary.getTranslation(eq("you"), eq("PRP"))).thenReturn("you");
        //when(mockDictonary.getTranslation(eq("."), eq("."))).thenReturn(".");

        String result = translationService.performTranslation(sourceText, "EN", "RO");

        assertEquals("I love you.", result, "Cuvintele netraduse ar terbui sa ramana in limba sursa.");
    }

    @Test
    void testConjugareVerb(){

        String sourceText = "I read a book.";

        String[] tokens = {"I", "read", "a", "book", "."};

        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"PRP", "VBP", "DT", "NN", "."};

        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        String[] lemmas = {"i", "read", "a", "book", "O"};

        when(mockLemmatizer.lemmatize(eq(tokens),eq(tags))).thenReturn(lemmas);

        when(mockDictonary.getTranslation(eq("i"), eq("PRP"))).thenReturn("eu");
        when(mockDictonary.getTranslation(eq("read"), eq("VBP"))).thenReturn("a citi");
        when(mockDictonary.getTranslation(eq("a"), eq("DT"))).thenReturn("o");
        when(mockDictonary.getTranslation(eq("book"), eq("NN"))).thenReturn("carte");

        String result = translationService.performTranslation(sourceText, "EN", "RO");

        assertEquals("Eu citu o carte.", result, "Verbul ar trebui sa fie conjugat pe baza subiectului 'i'");

    }

    @Test
    void testVerbConjugationWithReflexiveSubject() {
        String sourceText = "We work hard.";

        String[] tokens = {"We", "work", "hard", "."};
        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"PRP", "VBP", "RB", "."};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        String[] lemmas = {"we", "work", "hard", "O"};
        when(mockLemmatizer.lemmatize(eq(tokens), eq(tags))).thenReturn(lemmas);


        when(mockDictonary.getTranslation(eq("we"), eq("PRP"))).thenReturn("noi");

        when(mockDictonary.getTranslation(eq("work"), eq("VBP"))).thenReturn("a lucra");


        when(mockDictonary.getTranslation(eq("hard"), eq("RB"))).thenReturn("greu");

        String result = translationService.performTranslation(sourceText, "EN", "RO");

        assertEquals("Noi lucrm greu.", result, "Verbul 'a lucra' ar trebui să fie conjugat la 'noi'.");
    }

    @Test
    void testFormattingAndCapitalization() {
        String sourceText = "Do I know Java?";

        String[] tokens = {"Do", "I", "know", "Java", "?"};
        when(mockTokenizer.tokenize(sourceText)).thenReturn(tokens);

        String[] tags = {"VBP", "PRP", "VBP", "NNP", "?"};
        when(mockPosTagger.tag(tokens)).thenReturn(tags);

        String[] lemmas = {"do", "i", "know", "java", "O"};
        when(mockLemmatizer.lemmatize(eq(tokens), eq(tags))).thenReturn(lemmas);

        when(mockDictonary.getTranslation(eq("do"), eq("VBP"))).thenReturn(null);
        when(mockDictonary.getTranslation(eq("i"), eq("PRP"))).thenReturn("eu");
        when(mockDictonary.getTranslation(eq("know"), eq("VBP"))).thenReturn("a sti");
        when(mockDictonary.getTranslation(eq("java"), eq("NNP"))).thenReturn("Java");

        String result = translationService.performTranslation(sourceText, "EN", "RO");


        assertEquals("Do Eu stu Java?", result, "Trebuie sa gestioneze corect majusculele, spațiile, conjugarea si punctuația finală.");
    }
}