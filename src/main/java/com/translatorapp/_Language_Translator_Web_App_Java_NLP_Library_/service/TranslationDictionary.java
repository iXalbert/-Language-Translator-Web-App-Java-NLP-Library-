package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class TranslationDictionary {

    private final Map<String, String> dictionary = new HashMap<>();

    @Value("${app.dictionary.path.en}")
    private Resource dictionaryFile;

    private String createKey(String word, String posTag){

        return word.toLowerCase() + "_" + posTag;
    }

    @PostConstruct
    public void init(){

        System.out.println("Se initializeaza dictionarul ");

        try(BufferedReader reader = new BufferedReader(

                new InputStreamReader(dictionaryFile.getInputStream()))){

            String line;

            while ((line = reader.readLine()) != null){

                if(line.trim().isEmpty()){
                    continue;
                }

                String[] parts = line.split(",", 3);

                if(parts.length == 3){

                    String sourceWord = parts[0].trim().toLowerCase();
                    String posTag = parts[1].trim();
                    String translation = parts[2].trim();

                    String key = createKey(sourceWord,posTag);
                    dictionary.put(key,translation);

                    System.out.println("DEBUG : Cheia stocata : " + key);
                }else {
                    System.err.println("Avertisment : Linia de dictionar e incorecta asteapta 3 parametrii " + line);
                }
            }

            System.out.println("Dictionarul s a initializazt cu succes");
        } catch (Exception e){
            System.err.println("Eroare critica la fisierul de dictionar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getTranslation(String cleanWord, String posTag){

        /*
        String translated = dictionary.get(createKey(cleanWord,posTag));

        if(translated == null){
            translated = dictionary.get(cleanWord);
        }

        return translated;
        */
        return dictionary.get(createKey(cleanWord, posTag));
    }
}