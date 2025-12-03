package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TranslationDictionary {

    private final Map<String, String> dictionary = new HashMap<>();

    private String createKey(String word, String posTag){

        return word.toLowerCase() + "_" + posTag;
    }

    @PostConstruct
    public void init(){

        System.out.println("Se initializeaza dictionarul ");

        dictionary.put(createKey("hey", "NNP"), "salut");
        dictionary.put(createKey("world", "NN"), "lume");
        dictionary.put(createKey("this", "DT"), "aceasta");
        dictionary.put(createKey("is", "VBZ"), "este");
        dictionary.put(createKey("a", "DT"), "o");
        dictionary.put(createKey("test", "NN"), "proba");
        dictionary.put(createKey("java", "NNP"), "java");
        dictionary.put(createKey("application", "NN"), "aplicatie");
        dictionary.put(createKey(".","."), ".");
        dictionary.put(createKey("?", "?"), "?");
        dictionary.put(createKey("!", "."), "!");

        dictionary.put(createKey("run", "NN"), "alergare");
        dictionary.put(createKey("run", "VB"), "a alerga");

        System.out.println("Dictionarul s a initializazt cu succes");
    }

    public String getTranslation(String cleanWord, String posTag){

        String translated = dictionary.get(createKey(cleanWord,posTag));

        if(translated == null){
            translated = dictionary.get(cleanWord);
        }

        return translated;
    }
}