package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequest{
    private String sourceText;

    private String sourceLang;
    private String targetLang;

    public String getSourceText(){
        return sourceText;
    }

    public void setSourceText(String sourceText){
        this.sourceText = sourceText;
    }
}