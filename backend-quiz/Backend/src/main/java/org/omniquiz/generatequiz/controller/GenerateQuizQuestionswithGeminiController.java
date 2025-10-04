package org.omniquiz.generatequiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.omniquiz.generatequiz.dto.GenerateQuizRequest;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.omniquiz.generatequiz.service.GenerateQuizQuestionswithGeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
@RestController
@RequestMapping("/v1-api/quiz")
public class GenerateQuizQuestionswithGeminiController {
    private final GenerateQuizQuestionswithGeminiService generateQuizQuestionswithGeminiService;

    @Autowired
    public GenerateQuizQuestionswithGeminiController(
            GenerateQuizQuestionswithGeminiService generateQuizQuestionswithGeminiService) {
        this.generateQuizQuestionswithGeminiService = generateQuizQuestionswithGeminiService;
    }

    @PostMapping("/generate-questions")
    public ResponseEntity<List<GeneratedQuizQuestionsDTO>> generateQuizQuestions(@RequestBody GenerateQuizRequest body) throws JsonProcessingException {
        try {
            String prompt = body.getPrompt();
            System.out.println(prompt);
            List<GeneratedQuizQuestionsDTO> questions = generateQuizQuestionswithGeminiService.generateContent(prompt);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
