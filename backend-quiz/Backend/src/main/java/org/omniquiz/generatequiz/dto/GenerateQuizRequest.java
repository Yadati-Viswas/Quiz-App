package org.omniquiz.generatequiz.dto;

public class GenerateQuizRequest {

    private String prompt;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    // Default constructor is needed for JSON deserialization
    public GenerateQuizRequest() {
    }
}

