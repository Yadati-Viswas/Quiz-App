package org.omniquiz.generatequiz.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GenerateQuizQuestionswithGeminiService {
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
    public GenerateQuizQuestionswithGeminiService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.mapper = new ObjectMapper();
    }

    public List<GeneratedQuizQuestionsDTO> generateContent(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            System.out.println("Invalid prompt provided: null or empty");
            return Collections.emptyList();
        }

        try {
            // Step 1: Get initial response
            String rawResponse = chatModel.call(prompt);
            System.out.println("Raw Response: " + rawResponse);

            // Step 2: Clean and try parsing
            String cleanedResponse = cleanResponse(rawResponse);
            System.out.println("Cleaned Response: " + cleanedResponse);

            return parseWithRefinement(cleanedResponse, rawResponse, 0);
        } catch (Exception e) {
            System.out.println("Unexpected error during generation: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<GeneratedQuizQuestionsDTO> parseWithRefinement(String response, String rawResponse, int attempt) throws JsonProcessingException {
        try {
            if (response.isEmpty() || (!response.startsWith("[") && !response.startsWith("{"))) {
                throw new JsonParseException(null, "Invalid JSON format: " + response);
            }
            return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, GeneratedQuizQuestionsDTO.class));
        } catch (JsonProcessingException e) {
            System.out.println("Parsing failed (attempt " + attempt + "): " + e.getMessage());
            if (attempt < MAX_REFINEMENT_ATTEMPTS) {
                return refineAndParse(rawResponse, attempt + 1);
            }
            System.out.println("All Attempts Excceeded:");
            return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, GeneratedQuizQuestionsDTO.class));
        }
    }

    private List<GeneratedQuizQuestionsDTO> refineAndParse(String rawResponse, int attempt) throws JsonProcessingException {
        try {
            // Create refinement prompt
            String refinementPrompt = String.format(refinementPromptTemplate, rawResponse);

            // Call model again for refinement
            String refinedResponse = chatModel.call(refinementPrompt);
            System.out.println("Refined Response (Attempt " + attempt + "): " + refinedResponse);

            // Clean and parse refined response
            String cleanedRefined = cleanResponse(refinedResponse);
            System.out.println("Cleaned Refined Response (Attempt " + attempt + "): " + cleanedRefined);

            return parseWithRefinement(cleanedRefined, rawResponse, attempt);
        } catch (Exception e) {
            System.out.println("Refinement failed (attempt " + attempt + "): " + e.getMessage());
            if (attempt < MAX_REFINEMENT_ATTEMPTS) {
                return refineAndParse(rawResponse, attempt + 1); // Recursive retry
            }
            return parseWithRefinement(rawResponse, rawResponse, attempt + 1);
        }
    }

    private String cleanResponse(String rawResponse) {
        return rawResponse.trim()
                .replaceAll("^\\s*\\\\+\\s*", "") // Remove leading backslashes and whitespace
                .replaceAll("(?s)^AssistantMessage \\[.*?textContent=\\s*", "")
                .replaceAll("(?s)^```json\\s*|\\s*```$|^```\\s*|\\s*```$", "")
                .replaceAll(",\\s*metadata=\\{.*?\\}\\]$", "]")
                .replaceAll("(?<!\\\\)\\n", "\\\\n")
                .replaceAll("(?<!\\\\)\\r", "\\\\r")
                .replaceAll("(?<!\\\\)\\t", "\\\\t")
                .replaceAll("\\[\\s*\\]", "[]");
    }
}