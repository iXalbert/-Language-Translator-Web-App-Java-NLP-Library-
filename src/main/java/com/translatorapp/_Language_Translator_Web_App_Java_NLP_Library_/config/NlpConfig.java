package com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.config;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Configuration
public class NlpConfig {

    @Value("#{${nlp.token}}")
    private Map<String, Response> tokenModelResources;

    @Value("#{${nlp.pos}}")
    private Map<String, Response> posModelResources;

    @Value("#{${nlp.models.lemmatizer")
    private Map<String, Response> lemmatizerDictResources;

    @Bean
    public Map<String, TokenizerME> tokenizerMEMap(){

        return loadNlpModels(tokenModelResources, TokenizerModel::new, TokenizerME::new, "Tokenzier" );
    }

    @Bean
    public Map<String, POSTaggerME> posTaggerMEMap(){

        return loadNlpModels(posModelResources, POSModel::new, POSTaggerME:: new, "Tagger");
    }

    @Bean
    public Map<String, DictionaryLemmatizer> lemmatizerMap(){

        return loadNlpModels(lemmatizerDictResources, TokenizerModel::new, TokenizerME:: new, "Lemmatizer Dictionary");
    }

    private <M, T> Map<String, T> loadNlpModels(

            Map<String, Resource> resources,
            ModelCreator<M> modelCreator,
            InstanceCreator<M, T> instanceCreator,
            String modelNume){

        return resources.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry->{
                            try(InputStream is = entry.getKey().getInputStream()){
                                System.out.println("Incarcare : " + modelNume + "pentru limba : " + entry.getKey());
                                if(modelCreator.getClass().equals(DictionaryLemmatizer.ModelCreator.class)){
                                    return (T) new DictionaryLemmatizer((is));
                                }
                                M model = modelCreator.create(is);
                                return instanceCreator.create(model);
                            }catch (IOException e){
                                throw new IllegalStateException(
                                        "Eroare la incarcare " + modelNume + " pentru " + entry.getKey(), e
                                );
                            }
                        }
                ));
    }

    private Map<String, DictionaryLemmatizer> loadLemmatizerDictionaries(Map<String, Resource> resources) {
        return resources.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            try (InputStream is = entry.getValue().getInputStream()) {
                                System.out.println("Încărcare Lemmatizer Dictionary pentru limba: " + entry.getKey());
                                return new DictionaryLemmatizer(is);
                            } catch (IOException e) {
                                throw new IllegalStateException(
                                        "Eroare la încărcarea Lemmatizer Dictionary pentru " + entry.getKey(), e);
                            }
                        }
                ));
    }

    @FunctionalInterface
    interface ModelCreator<M>{
        M create(InputStream is) throws IOException;
    }

    @FunctionalInterface
    interface InstanceCreator<M, T>{
        T create(M model);
    }
}
