package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import java.io.InputStream;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
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


    @Value("classpath:models/en-lemmatizer.dict")
    private Resource lemmatizerModelResource;

    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;

    @Autowired
    public TranslationService(TranslationDictionary translationDictionary){
        this.translationDictionary = translationDictionary;
    }


    @PostConstruct
    public void init() {
        try (InputStream modelIn = modelResource.getInputStream()) {
            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);
            System.out.println("--- Modelul OpenNLP '" + modelResource.getFilename() + "' a fost încărcat cu succes! ---");
        } catch (IOException e) {
            throw new IllegalStateException("Eroare I/O la Tokenizer (Fișier inaccesibil sau corupt).", e);
        } catch (Exception e) {
            throw new IllegalStateException("Eroare critică necunoscută la inițializarea Tokenizer.", e);
        }

        try(InputStream modelIn = posModelResource.getInputStream()){
            if(modelIn == null){
                throw new IllegalStateException("Fatal error : Resursa POS nu a fost gasita ");
            }
            POSModel model = new POSModel(modelIn);
            this.posTagger = new POSTaggerME(model);
            System.out.println("Modelul OpenNLP POS Tagger a fost incarcat cu succes");
        }catch (IOException e){
            throw new IllegalStateException("Eroare la incarcarea modelului OpenNLP POS Tagger");
        }catch (Exception e){
            throw new IllegalStateException("Erroare critica necunoscuta la POS Tagger");
        }


        try(InputStream modelIn = getClass().getResourceAsStream("/models/en-lemmatizer.dict")){
            if(modelIn == null){
                // Dacă stream-ul este null, fișierul lipsește din Classpath!
                throw new IllegalStateException("FATAL: Resursa 'en-lemmatizer.dict' nu a fost găsita în src/main/resources/models/. Verificați calea!");
            }
            lemmatizer = new DictionaryLemmatizer(modelIn);
            System.out.println("Lemmatizer a fost integrat cu succes");
        }catch (Exception e){
            // Aruncăm excepția reală
            throw new IllegalStateException("Eroare la incarcarea Lemmatizer-ului. Verificati formatul fisierului .dict", e);
        }
    }

    private String conjugaVerb(String verb, String subject){

        if(!verb.startsWith("a ")){
            return verb;
        }

        String baseVerb = verb.substring(2).trim();
        String stem;

        if(baseVerb.endsWith("a")){
            stem = baseVerb.substring(0, baseVerb.length() - 1);
        }
        else if(baseVerb.endsWith("i")){
            stem = baseVerb.substring(0, baseVerb.length() - 1);
        }
        else{
            stem = baseVerb;
        }

        switch (subject.toLowerCase()){

            case "i":
                if(stem.endsWith("g") || stem.endsWith("c")){
                    return stem;
                }
                return stem + "u";

            case "you":
                return stem + "i";

            case "he":
            case "she":
            case "it":
                if(baseVerb.endsWith("a")){
                    return stem + "a";
                }
                return baseVerb;

            case "we":
                return stem + "m";

            case "they":
                if(baseVerb.endsWith("a")){
                    return stem + "a";
                }
                return baseVerb;

            default:
                return baseVerb;
        }
    }

    public String performTranslation(String sourceText, String sourceLang, String targetLang) {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "Te rog să introduci text pentru traducere.";
        }

        String[] tokens = tokenizer.tokenize(sourceText);
        String[] tags = posTagger.tag(tokens);
        String[] lemmas = lemmatizer.lemmatize(tokens, tags);

        StringBuilder resultBuilder = new StringBuilder();
        String last_subject = null;

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String tag = tags[i];
            String lemma = lemmas[i];

            System.out.println("DEBUG: Token = " + token + ", Tag POS = " + tag + ", Lemma = " + lemma);

            String wordToTranslate;
            String translatedWord;


            if (lemma != null && !lemma.equals("O") && !tag.matches("\\p{Punct}")) {
                wordToTranslate = lemma;
            } else {
                wordToTranslate = token;
            }


            if (tags[i].startsWith("PRP") || tags[i].startsWith("NNP")) {
                String potentialSubject = wordToTranslate.toLowerCase();
                if (potentialSubject.equals("i") || potentialSubject.equals("you") || potentialSubject.equals("he") ||
                        potentialSubject.equals("she") || potentialSubject.equals("it") || potentialSubject.equals("we") ||
                        potentialSubject.equals("they")) {
                    last_subject = potentialSubject;
                }
            }


            if (tag.matches("\\p{Punct}")) {
                translatedWord = token;
            } else {
                translatedWord = translationDictionary.getTranslation(wordToTranslate.toLowerCase(), tag);

                if (translatedWord == null) {
                    translatedWord = token;
                } else if (tags[i].startsWith("VB") && translatedWord.startsWith("a ") && last_subject != null) {

                    translatedWord = conjugaVerb(translatedWord, last_subject);
                }
            }

            if (Character.isUpperCase(token.charAt(0)) && translatedWord.length() > 0) {
                translatedWord = Character.toUpperCase(translatedWord.charAt(0)) + translatedWord.substring(1);
            }


            if (i > 0 && !token.matches("\\p{Punct}")) {
                resultBuilder.append(" ");
            }

            resultBuilder.append(translatedWord);
        }

        return resultBuilder.toString().trim();
    }
}