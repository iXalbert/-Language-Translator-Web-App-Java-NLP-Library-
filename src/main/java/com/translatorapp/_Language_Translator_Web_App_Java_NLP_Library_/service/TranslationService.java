package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import java.io.InputStream;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Service
public class TranslationService {

    private final TranslationDictionary translationDictionary;

    @Value("classpath:en-token/token.bin")
    private Resource modelResource;

    @Value("classpath:en-pos/pos.bin")
    private Resource posModelResource;

    private TokenizerME tokenizer;
    private POSTaggerME posTagger;

    //private final Map<String, String> dictionary =  new HashMap<>();

    @Autowired
    public TranslationService(TranslationDictionary translationDictionary){
        this.translationDictionary = translationDictionary;
    }


    @PostConstruct
    public void init() {
        String resourcePath = "en-token/token.bin";
        System.out.println("DEBUG: Se încearcă încărcarea modelului din calea: " + modelResource);

        try (InputStream modelIn = modelResource.getInputStream()) {

            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);

            if (modelIn == null) {
                throw new IllegalStateException("FATAL: Injectarea resursei a eșuat. Verificati daca fișierul există la calea specificată.");
            }


            System.out.println("--- Modelul OpenNLP '" + modelResource.getFilename() + "' a fost încărcat cu succes! ---");

            /*dictionary.put("hello", "salut");
            dictionary.put("world", "lume");
            dictionary.put("this", "aceasta");
            dictionary.put("is", "este");
            dictionary.put("a", "o");
            dictionary.put("test", "proba");
            dictionary.put("java", "java");
            dictionary.put("application", "aplicatie");
            dictionary.put(".", ".");
            dictionary.put("?", "?");
            dictionary.put("!", "!");*/

        } catch (IOException e) {
            throw new IllegalStateException("Eroare I/O (Fișier inaccesibil sau corupt).", e);
        } catch (Exception e) {

            throw new IllegalStateException("Eroare critică necunoscută la inițializarea modelului OpenNLP. Vă rugăm să verificați integritatea fișierului model.", e);
        }

        try(InputStream modelIn = posModelResource.getInputStream()){

            if(modelIn == null){
               throw new IllegalStateException("Fatal error : Resursa POS nu a fost gaita ");
            }

            POSModel model = new POSModel(modelIn);
            this.posTagger = new POSTaggerME(model);

            System.out.println("Modelul OpenNLP POS Tagger a fost incarcat cu succes");
        }catch (IOException e){
            throw new IllegalStateException("Eroare la incarcarea modelului OpenNLP POS Tagger");
        }catch (Exception e){
            throw new IllegalStateException("Erroare critica necunoscuta ");
        }
    }

    public String performTranslation(String sourceText, String sourceLang, String targetLang) {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "Te rog să introduci text pentru traducere.";
        }

        String[] tokens = tokenizer.tokenize(sourceText);
        String[] tags = posTagger.tag(tokens);

        StringBuilder resultBuilder = new StringBuilder();

        for(int i=0;i< tokens.length;i++){

            String token = tokens[i];

            String cleanToken;

            if (token.matches("^[\\.,;:\\?!]$")) {
                cleanToken = token;
            } else {

                cleanToken = token.replaceAll("[\\.,;:\\?!]", "").toLowerCase();
            }

            String translatedWord = translationDictionary. getTranslation(cleanToken, tags[i]);

            if(translatedWord == null){

                translatedWord = token;
            }

            if(Character.isUpperCase(token.charAt(0)) && translatedWord.length() > 0){
                translatedWord = Character.toUpperCase(translatedWord.charAt(0)) + translatedWord.substring(1);
            }

            if(i>0 && !token.matches("^[\\.,;:\\?!]$")){
                resultBuilder.append(" ");
            }

            resultBuilder.append(translatedWord);
        }

        return resultBuilder.toString().trim();

        /*String translatedText = Arrays.stream(tokens)
                .map(token -> {
                    String lowerToken = token.toLowerCase();
                    
                    return dictionary.getOrDefault(lowerToken,token);
                })
                .collect(Collectors.joining(" "));*/
        
        //String processedText = Arrays.stream(tokens).collect(Collectors.joining(" | "));

        //return translatedText;
    /*
        return String.format(
                "Placeholder : Traducerea (Token-uri : %s) din %s in %s. Text original : '%s' ",
                translatedText,
                sourceLang,
                targetLang,
                sourceText
        );
        */
    }
}