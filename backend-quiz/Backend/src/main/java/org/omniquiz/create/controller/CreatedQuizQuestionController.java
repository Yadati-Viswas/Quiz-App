package org.omniquiz.create.controller;


import jakarta.validation.Valid;
import org.omniquiz.create.dto.CreatedQuizQuestionDTO;
import org.omniquiz.create.service.CreatedQuizQuestionService;
import org.omniquiz.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1-api/quiz")
public class CreatedQuizQuestionController {

    @Autowired
    private CreatedQuizQuestionService createdQuizQuestionService;

    @PostMapping("/create")
    public ResponseEntity<?> getCreateQuizQuestions(@Valid @RequestBody CreatedQuizQuestionDTO createdQuizQuestionDTO, @AuthenticationPrincipal User user) {
        try{
            System.out.println(createdQuizQuestionDTO.getTitle());
            String response = createdQuizQuestionService.saveQuizQuestions(createdQuizQuestionDTO,user);

            return ResponseEntity.ok(200);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(401);
        }

    }
}
