package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service; // Pachetul tau

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import java.io.IOException;

@Service
public class TranslationService {

    @Value("classpath:en-token.bin")
    private Resource modelResource;

    private TokenizerME tokenizer;

    @PostConstruct
    public void init() {
        try (InputStream modelIn = modelResource.getInputStream()) {

            if (modelIn == null) {
                throw new IllegalStateException("FATAL: Injectarea resursei nu a functionat.");
            }


            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);


            System.out.println("--- Modelul OpenNLP 'en-token.bin' a fost încărcat cu succes! ---");

        } catch (IOException e) {
            throw new IllegalStateException("Eroare la citirea modelului OpenNLP (Verificati daca fisierul nu e corupt!)", e);
        } catch (Exception e) {
            throw new IllegalStateException("Eroare necunoscută la inițializarea modelului OpenNLP.", e);
        }
    }

    public String performTranslation(String sourceText) {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "Te rog să introduci text pentru traducere.";
        }


        String[] tokens = tokenizer.tokenize(sourceText);

        String processedText = Arrays.stream(tokens).collect(Collectors.joining(" | "));

        return "Tokens procesați: " + processedText;
    }
}