package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.LanguageTranslatorWebAppJavaNlpLibraryApplication;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.controller.TranslationController;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.exception.GlobalExceptionHandler;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.model.TranslationRequest;
import com.translatorapp._Language_Translator_Web_App_Java_NLP_Library_.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
@ContextConfiguration(classes = {TranslationController.class, LanguageTranslatorWebAppJavaNlpLibraryApplication.class})
@Import(GlobalExceptionHandler.class)
public class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TranslationService mockTranslationService;

    private static final String API_URL = "/api/v1";

    @Test
    void translate_ShouldReturnTranslatedText_WhenSuccessful() throws Exception{

        TranslationRequest request = new TranslationRequest("Hello world", "EN", "RO");
        String expectedTranslation = "Salut lume";

        when(mockTranslationService.performTranslation(any(),any(),any())).thenReturn(expectedTranslation);

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translatedText").value(expectedTranslation))
                .andExpect(jsonPath("$.statusMessage").value("Traducerea efectuata cu succes"));
    }

    @Test
    void translate_ShouldReturnBadRequest_WhenSourceTextIsEmpty() throws Exception {

        TranslationRequest request = new TranslationRequest(" ", "EN", "RO");
        String expectedMessage = "Eroare de validare: Textul sursa nu poate fi gol";

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.translatedText").doesNotExist())
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    void translate_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {

        TranslationRequest request = new TranslationRequest("Error case", "EN", "RO");
        String errorMessage = "NLP Model failed to load.";

        when(mockTranslationService.performTranslation(any(), any(), any()))
                .thenThrow(new RuntimeException(errorMessage));

        String expectedMessage = "Eroare interna a serverului: " + errorMessage;

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.translatedText").doesNotExist())
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }
}
