package model;

public class TranslationResponse {
    private String translatedText;

    private String statusMessage;

    public TranslationResponse(String translatedText, String statusMessage){

        this.translatedText = translatedText;
        this.statusMessage = statusMessage;
    }

    public String getTranslatedText(){
        return translatedText;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
