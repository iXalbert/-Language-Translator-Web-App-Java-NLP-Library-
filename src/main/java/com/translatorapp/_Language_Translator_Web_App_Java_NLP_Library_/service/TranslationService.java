package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service; // Pachetul tau

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Service
public class TranslationService {

    private TokenizerME tokenizer;

    @PostConstruct
    public void init() {
        try (InputStream modelIn = Thread.currentThread().getContextClassLoader().getResourceAsStream("en-token.bin")) {


            if (modelIn == null) {
                throw new IllegalStateException("FATAL: Resursa 'en-token.bin' nu a fost găsită în classpath (verificați src/main/resources)!");
            }

            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);
        } catch (Exception e) {
            throw new IllegalStateException("Nu am putut încărca modelul OpenNLP!", e);
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