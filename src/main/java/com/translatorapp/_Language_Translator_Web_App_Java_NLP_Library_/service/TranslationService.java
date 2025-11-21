package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import org.springframework.stereotype.Service;

@Service
public class TranslationService{

    public String performTranslation(String sourceText){

        // Logica pentru testul initial

        if(sourceText == null || sourceText.trim().isEmpty()){

            return "Te rog sa introduci text pentru a putea traduce";
        }

        String lowerCaseText = sourceText.toLowerCase();
        if(lowerCaseText.contains("salut")){
            return "Hello";
        }else if(lowerCaseText.contains("codare")){
            return "Coing...";
        }

        return "Traducere pentru : " + sourceText;
    }
}