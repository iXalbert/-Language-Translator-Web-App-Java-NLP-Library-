package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.controller;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationRequest;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationResponse;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service.TranslationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")

public class TranslationController{

    @Autowired
    private TranslationService translationService;

    @PostMapping("/translate")
    public TranslationResponse translate(@RequestBody TranslationRequest request){

        String sourceText = request.getSourceText();
        String translatedText = translationService.performTranslation(sourceText);

        return new TranslationResponse(translatedText);
    }
}