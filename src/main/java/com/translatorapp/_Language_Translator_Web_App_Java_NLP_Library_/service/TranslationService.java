package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service;

import java.util.Map;

import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.exception.TranslationValidationException;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;

@Service
public class TranslationService {

    private final TranslationDictionary translationDictionary;

    private final Map<String, TokenizerME> tokenizerMap;
    private final Map<String, POSTaggerME> posTaggerMap;
    private final Map<String, DictionaryLemmatizer> lemmatizerMap;

    private String last_subject = null;

    @Autowired
    public TranslationService(TranslationDictionary translationDictionary,
                              Map<String,TokenizerME> tokenizerMap,
                              Map<String,POSTaggerME> posTaggerMap,
                              Map<String,DictionaryLemmatizer> lemmatizerMap
    ){
        this.translationDictionary = translationDictionary;
        this.tokenizerMap = tokenizerMap;
        this.posTaggerMap = posTaggerMap;
        this.lemmatizerMap = lemmatizerMap;
    }

    private String getAuxiliary(String subject) {
        switch (subject.toLowerCase()) {
            case "i": return "am";
            case "you": return "ai";
            case "he":
            case "she":
            case "it": return "a";
            case "we": return "am";
            case "they": return "au";
            default: return "";
        }
    }

    private String getPastParticiple(String verbInfinitive) {
        if (!verbInfinitive.startsWith("a ")) {
            return verbInfinitive;
        }
        String stem = verbInfinitive.substring(2).trim();

        if (stem.endsWith("a")) {
            return stem.substring(0, stem.length() - 1) + "at";
        }

        if (stem.endsWith("i")) {
            if (stem.equals("fugi")) {
                return "fugit";
            }
            return stem.substring(0, stem.length() - 1) + "it";
        }

        return stem;
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
    @Cacheable("translations")
    public String performTranslation(String sourceText, String sourceLang, String targetLang) {


        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "Te rog să introduci text pentru traducere.";
        }

        System.out.println("Executa logica de traducere complexa (nu din cache)");

        String sourceLangKey = sourceLang.toLowerCase();

        if(!tokenizerMap.containsKey(sourceLangKey)){

            throw new TranslationValidationException("limba sursa '" + sourceLang + "' nu e suportata momentan");
        }

        TokenizerME currentTokenizer = tokenizerMap.get(sourceLangKey);
        POSTaggerME currentPOSTagger = posTaggerMap.get(sourceLangKey);
        DictionaryLemmatizer currentLemmatizer = lemmatizerMap.get(sourceLangKey);

        String[] tokens = currentTokenizer.tokenize(sourceText);
        String[] tags = currentPOSTagger.tag(tokens);
        String[] lemmas = currentLemmatizer.lemmatize(tokens, tags);

        StringBuilder resultBuilder = new StringBuilder();
        last_subject = null;

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
                }

                else if (tag.startsWith("VBD") && translatedWord.startsWith("a ") && last_subject != null) {

                    String participle = getPastParticiple(translatedWord);
                    String auxiliary = getAuxiliary(last_subject);

                    translatedWord = auxiliary + " " + participle;
                }

                else if (tags[i].startsWith("VB") && translatedWord.startsWith("a ") && last_subject != null) {

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