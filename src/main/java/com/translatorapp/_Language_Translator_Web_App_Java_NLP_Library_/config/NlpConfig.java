package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.config;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class NlpConfig {

    private final ResourceLoader resourceLoader;

    @Value("#{${nlp.token}}")
    private Map<String, String> tokenModelPaths;

    @Value("#{${nlp.pos}}")
    private Map<String, String> posModelPaths;

    @Value("#{${nlp.models.lemmatizer}}")
    private Map<String, String> lemmatizerDictPaths;

    public NlpConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Map<String, TokenizerME> tokenizerMap() {
        return loadNlpModels(tokenModelPaths, TokenizerModel::new, TokenizerME::new, "Tokenizer");
    }

    @Bean
    public Map<String, POSTaggerME> posTaggerMap() {
        return loadNlpModels(posModelPaths, POSModel::new, POSTaggerME::new, "POS Tagger");
    }

    @Bean
    public Map<String, DictionaryLemmatizer> lemmatizerMap() {
        return loadLemmatizerDictionaries(lemmatizerDictPaths);
    }

    private <M, T> Map<String, T> loadNlpModels(
            Map<String, String> paths,
            ModelCreator<M> modelCreator,
            InstanceCreator<M, T> instanceCreator,
            String modelName) {

        return paths.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Resource resource = resourceLoader.getResource(entry.getValue());
                            try (InputStream is = resource.getInputStream()) {
                                System.out.println("Încărcare " + modelName + " pentru limba: " + entry.getKey());
                                M model = modelCreator.create(is);
                                return instanceCreator.create(model);
                            } catch (IOException e) {
                                throw new IllegalStateException(
                                        "Eroare la încărcarea " + modelName + " (" + entry.getValue() + ") pentru " + entry.getKey(), e);
                            }
                        }
                ));
    }

    private Map<String, DictionaryLemmatizer> loadLemmatizerDictionaries(Map<String, String> paths) {
        return paths.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Resource resource = resourceLoader.getResource(entry.getValue());
                            try (InputStream is = resource.getInputStream()) {
                                System.out.println("Încărcare Lemmatizer Dictionary pentru limba: " + entry.getKey());
                                return new DictionaryLemmatizer(is);
                            } catch (IOException e) {
                                throw new IllegalStateException(
                                        "Eroare la încărcarea Lemmatizer Dictionary (" + entry.getValue() + ") pentru " + entry.getKey(), e);
                            }
                        }
                ));
    }

    @FunctionalInterface
    interface ModelCreator<M> {
        M create(InputStream is) throws IOException;
    }

    @FunctionalInterface
    interface InstanceCreator<M, T> {
        T create(M model);
    }
}