package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TranslationDictionary {

    private final Map<String, String> dictionary = new HashMap<>();

    @PostConstruct
    public void init(){

        System.out.println("Se initializeaza dictionarul ");

        dictionary.put("hello", "salut");
        dictionary.put("world", "lume");
        dictionary.put("this", "aceasta");
        dictionary.put("is", "este");
        dictionary.put("a", "o");
        dictionary.put("test", "proba");
        dictionary.put("java", "java");
        dictionary.put("application", "aplicatie");
        dictionary.put(".", ".");
        dictionary.put("?", "?");
        dictionary.put("!", "!");

        System.out.println("Dictionarul s a initializazt cu succes");
    }

    public String getTranslation(String word){

        return dictionary.get(word);
    }
}
