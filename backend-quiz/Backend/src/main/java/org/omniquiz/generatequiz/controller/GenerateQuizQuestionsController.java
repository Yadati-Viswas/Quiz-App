package org.omniquiz.generatequiz.controller;

import jakarta.transaction.Transactional;
import org.omniquiz.generatequiz.dto.GenerateQuizRequest;
import org.omniquiz.generatequiz.dto.GeneratedQuizQuestionsDTO;
import org.omniquiz.generatequiz.model.GeneratedQuiz;
import org.omniquiz.generatequiz.model.GeneratedQuizQuestion;
import org.omniquiz.generatequiz.repository.GeneratedQuizQuestionRepository;
import org.omniquiz.generatequiz.repository.GeneratedQuizRepository;
import org.omniquiz.generatequiz.service.GenerateQuizQuestionsService;
import org.omniquiz.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1-api/quiz")
public class GenerateQuizQuestionsController {
    private final GenerateQuizQuestionsService generateQuizQuestionsService;

    private User user;

    @Autowired
    private GeneratedQuizRepository generatedQuizRepository;
    @Autowired
    private GeneratedQuizQuestionRepository generatedQuizQuestionRepository;

    @Autowired
    public GenerateQuizQuestionsController(
            GenerateQuizQuestionsService generateQuizQuestionsService) {
        this.generateQuizQuestionsService = generateQuizQuestionsService;
    }

    @PostMapping("/generate-questions")
    public ResponseEntity<?> generateQuizQuestions(@RequestBody GenerateQuizRequest body, @AuthenticationPrincipal User user) {
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
        List<GeneratedQuizQuestionsDTO> questions = getAllGeneratedQuestions(prompt);
        saveGeneratedQuestions(questions, user);
        return ResponseEntity.ok(questions);
    }

    public List<GeneratedQuizQuestionsDTO> getAllGeneratedQuestions(String prompt) {
        int totalQuestions = 5;
        int batchSize = 5;
        int totalBatches = totalQuestions / batchSize;
        int remainingQuestions = totalQuestions % batchSize;

        List<CompletableFuture<List<GeneratedQuizQuestionsDTO>>> futures = new ArrayList<>();
        for (int i = 0; i < totalBatches; i++) {
            String batchPrompt = prompt.replaceFirst("\\d+", String.valueOf(batchSize));
            System.out.println("Batch Prompt: " + batchPrompt);
            futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionsService.generateContent(batchPrompt)));
        }

        if (remainingQuestions > 0) {
            String remainingPrompt = prompt.replaceFirst("\\d+", String.valueOf(remainingQuestions));
            System.out.println("Remaining Prompt: " + remainingPrompt);
            futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionsService.generateContent(remainingPrompt)));
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
                futures.add(CompletableFuture.supplyAsync(() -> generateQuizQuestionsService.generateContent(batchPrompt)));
            }
            questions.add((GeneratedQuizQuestionsDTO) futures.stream().map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList()));
        }
        return  questions;
    }

    @Transactional
    public void saveGeneratedQuestions(List<GeneratedQuizQuestionsDTO> questions, User user) {

        String title = questions.get(0).getTitle();
        GeneratedQuiz quiz = new GeneratedQuiz();
        quiz.setTitle(title);
        quiz.setUser(user);
        quiz.setCreatedAt(LocalDateTime.now());

        quiz = generatedQuizRepository.save(quiz);
        for (GeneratedQuizQuestionsDTO question : questions) {
            GeneratedQuizQuestion q = new GeneratedQuizQuestion();
            q.setQuestion(question.getQuestion());
            q.setCode(question.getCode());
            q.setOptions(question.getOptions());
            q.setAnswer(question.getAnswer());
            q.setExplanation(question.getExplanation());
            q.setUser(user);
            q.setGeneratedQuiz(quiz);

            quiz.addQuestion(q);
        }
        generatedQuizRepository.save(quiz);
    }
}