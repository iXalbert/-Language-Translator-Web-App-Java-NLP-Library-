package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.exception;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationRequest;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TranslationValidationException.class)
    public ResponseEntity<TranslationResponse> handleValidationException(TranslationValidationException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new TranslationResponse(null, "Eroare de validare: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TranslationResponse> handleAllException(Exception ex){

        System.err.println("Eroare neasteptata la traducere : " + ex.getMessage());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new TranslationResponse(null,"Eroare interna a serverului: " + ex.getMessage()));
    }
}
