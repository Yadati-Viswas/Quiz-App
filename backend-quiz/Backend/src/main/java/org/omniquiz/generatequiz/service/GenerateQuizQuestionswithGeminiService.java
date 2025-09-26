package org.omniquiz.generatequiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateQuizQuestionswithGeminiService {
    private final ChatModel chatModel;

    @Autowired
    public GenerateQuizQuestionswithGeminiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public List<GeneratedQuizQuestionsDTO> generateContent(String prompt) throws JsonProcessingException {
        String rawResponse = chatModel.call(prompt);
        System.out.println(rawResponse);
        rawResponse = rawResponse.trim();
        rawResponse = rawResponse.replaceAll("(?s)^```json\\s*|\\s*```$", "");
        rawResponse = rawResponse.replaceAll("(?s)^```\\s*|\\s*```$", "");
        System.out.println(rawResponse);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(rawResponse, mapper.getTypeFactory().constructCollectionType(List.class, GeneratedQuizQuestionsDTO.class));
    }
}