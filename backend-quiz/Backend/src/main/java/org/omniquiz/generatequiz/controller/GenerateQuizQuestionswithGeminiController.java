package org.omniquiz.generatequiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.omniquiz.generatequiz.dto.GenerateQuizRequest;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.omniquiz.generatequiz.service.GenerateQuizQuestionswithGeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> generateQuizQuestions(@RequestBody GenerateQuizRequest body) {
        String prompt = body.getPrompt();
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prompt cannot be empty");
        }
        System.out.println("Original Prompt: " + prompt);

        Pattern pattern = Pattern.compile("Generate\\s+(\\d+)\\s+multiple-choice\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(prompt);
        int totalQuestions = 5;
        if (matcher.find()) {
            totalQuestions = Integer.parseInt(matcher.group(1));
            if (totalQuestions <= 0) {
                return ResponseEntity.badRequest().body("Number of questions must be positive");
            }
        } else {
            System.out.println("Regex failed to match prompt: " + prompt);
            return ResponseEntity.badRequest().body("Prompt must specify number of questions (e.g., 'Generate 50 questions')");
        }

        int batchSize = 5;
        int totalBatches = totalQuestions / batchSize;
        int remainingQuestions = totalQuestions % batchSize;

        List<CompletableFuture<List<GeneratedQuizQuestionsDTO>>> futures = new ArrayList<>();
        for (int i = 0; i < totalBatches; i++) {
            String batchPrompt = prompt.replaceFirst("\\d+", String.valueOf(batchSize));
            System.out.println("Batch Prompt: " + batchPrompt);
            futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionswithGeminiService.generateContent(batchPrompt)));
        }

        if (remainingQuestions > 0) {
            String remainingPrompt = prompt.replaceFirst("\\d+", String.valueOf(remainingQuestions));
            System.out.println("Remaining Prompt: " + remainingPrompt);
            futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionswithGeminiService.generateContent(remainingPrompt)));
        }

        // Collect all results, including empty lists from failed batches
        List<GeneratedQuizQuestionsDTO> questions = futures.stream()
                .map(CompletableFuture::join) // Wait for all to complete
                .flatMap(List::stream)       // Flatten the lists
                .collect(Collectors.toList());

        // Check if total questions match the request (optional validation)
        if (questions.size() < totalQuestions) {
            int remainingQuestionstoGenerate = (totalQuestions - questions.size());
            System.out.println("Sending another API call to generate remaining questions: "+ remainingQuestionstoGenerate);
            remainingQuestionstoGenerate /=5;
            for (int i = 0; i < remainingQuestionstoGenerate/5; i++) {
                String batchPrompt = prompt.replaceFirst("\\d+", String.valueOf(batchSize));
                System.out.println("Batch Prompt: " + batchPrompt);
                futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionswithGeminiService.generateContent(batchPrompt)));
            }
        }

        return ResponseEntity.ok(questions);
    }
}