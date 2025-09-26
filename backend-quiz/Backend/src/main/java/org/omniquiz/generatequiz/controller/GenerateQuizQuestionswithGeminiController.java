package org.omniquiz.generatequiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.omniquiz.generatequiz.dto.GenerateQuizRequest;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.omniquiz.generatequiz.service.GenerateQuizQuestionswithGeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1-api/quiz")
public class GenerateQuizQuestionswithGeminiController {
    private final GenerateQuizQuestionswithGeminiService generateQuizQuestionswithGeminiService;

    @Autowired
    public GenerateQuizQuestionswithGeminiController(
            GenerateQuizQuestionswithGeminiService generateQuizQuestionswithGeminiService) {
        this.generateQuizQuestionswithGeminiService = generateQuizQuestionswithGeminiService;
    }

    @GetMapping("/generate-questions")
    public List<GeneratedQuizQuestionsDTO> generateQuizQuestions(@RequestBody GenerateQuizRequest body) throws JsonProcessingException {
        String prompt = body.getPrompt();
        List<GeneratedQuizQuestionsDTO> questions = generateQuizQuestionswithGeminiService.generateContent(prompt);
        return questions;
    }

}
