package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model;

public class TranslationResponse{
    private String translatedText;

    public TranslationResponse(String translatedText){
        this.translatedText = translatedText;
    }

    public String getTranslatedText(){
        return translatedText;
    }
}