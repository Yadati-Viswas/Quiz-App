package org.omniquiz.generatequiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GenerateQuizQuestionsService {
    private final ChatModel chatModel;
    private final ObjectMapper mapper;
    private static final int MAX_REFINEMENT_ATTEMPTS = 2; // Limit refinement retries
    private final String refinementPromptTemplate = """
            You previously generated a response to a prompt for quiz questions, but it is not a valid JSON format. Below is the raw response you returned:
            
            %s
            
            Extract ONLY the JSON array of quiz questions from this response and return it in valid JSON format. Do not include any other text, metadata, code blocks (e.g., ```json), or wrappers. The output must be strictly a JSON array like:
            [
              {
                "title": "Question 1: Topic",
                "question": "The question text?",
                "code": "Optional code block as a string, or empty string if none",
                "options": ["a) Option1", "b) Option2", "c) Option3", "d) Option4"],
                "answer": "c) The correct option",
                "explanation": "Detailed explanation here"
              }
            ]
            Ensure each question has exactly 4 options (a-d). Replace newlines in strings with \\n and ensure no unescaped control characters. The output must be parseable as valid JSON.
            """;

    @Autowired
    public GenerateQuizQuestionsService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.mapper = new ObjectMapper();
    }

    public List<GeneratedQuizQuestionsDTO> generateContent(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            System.out.println("Invalid prompt provided: null or empty");
            return Collections.emptyList();
        }

        try {
            String rawResponse = chatModel.call(prompt);
            System.out.println("Raw Response: " + rawResponse);
            return mapper.readValue(rawResponse, mapper.getTypeFactory().constructCollectionType(List.class, GeneratedQuizQuestionsDTO.class));
        } catch (Exception e) {
            System.out.println("Unexpected error during generation: " + e.getMessage());
            return Collections.emptyList();
        }
    }




}