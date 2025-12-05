package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model;

public class TranslationRequest{

    private String sourceText;
    private String sourceLang;
    private String targetLang;

    public TranslationRequest(String sourceText, String sourceLang, String targetLang){
        this.targetLang = targetLang;
        this.sourceLang = sourceLang;
        this.sourceText = sourceText;
    }

    public TranslationRequest(){

    }

    public String getSourceText(){
        return sourceText;
    }

    public void setSourceText(String sourceText){
        this.sourceText = sourceText;
    }

    public String getSourceLang(){
        return sourceLang;
    }

    public void setSourceLang(String sourceLang){
        this.sourceLang = sourceLang;
    }

    public String getTargetLang(){
        return targetLang;
    }

    public void setTargetLang(String targetLang){
        this.targetLang = targetLang;
    }

}