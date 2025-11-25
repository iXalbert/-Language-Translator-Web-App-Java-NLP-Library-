package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Service
public class TranslationService {


    @Value("classpath:en-token/token.bin")
    private Resource modelResource;

    private TokenizerME tokenizer;

    @PostConstruct
    public void init() {
        System.out.println("DEBUG: Se încearcă încărcarea modelului din calea: " + modelResource);

        try (InputStream modelIn = modelResource.getInputStream()) {

            if (modelIn == null) {
                throw new IllegalStateException("FATAL: Injectarea resursei a eșuat. Verificati daca fișierul există la calea specificată.");
            }


            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);

            System.out.println("--- Modelul OpenNLP '" + modelResource.getFilename() + "' a fost încărcat cu succes! ---");

        } catch (IOException e) {
            throw new IllegalStateException("Eroare I/O (Fișier inaccesibil sau corupt).", e);
        } catch (Exception e) {

            throw new IllegalStateException("Eroare critică necunoscută la inițializarea modelului OpenNLP. Vă rugăm să verificați integritatea fișierului model.", e);
        }
    }

    public String performTranslation(String sourceText, String sourceLang, String targetLang) {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "Te rog să introduci text pentru traducere.";
        }

        String[] tokens = tokenizer.tokenize(sourceText);
        String processedText = Arrays.stream(tokens).collect(Collectors.joining(" | "));

        return String.format(
                "Placeholder : Traducerea (Token-uri : %s) din %s in %s. Text original : '%s' ",
                processedText,
                sourceLang,
                targetLang,
                sourceText
        );
    }
}