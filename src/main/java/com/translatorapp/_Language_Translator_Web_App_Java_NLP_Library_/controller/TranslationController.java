package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.controller;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationRequest;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationResponse;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service.TranslationService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/v1")
//@Getter
//@Setter
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public ResponseEntity<TranslationResponse> translate(@RequestBody TranslationRequest request){

        try{
            if(request.getSourceText() == null || request.getSourceText().trim().isEmpty()){
                return ResponseEntity.badRequest().body(
                        new TranslationResponse(null,"Textul sursa nu poate fi gol")
                );
            }

            String translated = translationService.performTranslation(
                    request.getSourceText(),
                    request.getSourceLang(),
                    request.getTargetLang()
            );

            return ResponseEntity.ok(
                    new TranslationResponse(translated,"Traducerea efectuata cu succes")
            );
        }catch (Exception e){

                    System.err.println("Eroare la procesarea traducerii: " + e.getMessage());

                    e.printStackTrace();

                    return ResponseEntity.internalServerError().body(
                            new TranslationResponse(null, "Eroare interna a servarului la traducere :  " + e.getMessage())
                    );
        }
    }
}