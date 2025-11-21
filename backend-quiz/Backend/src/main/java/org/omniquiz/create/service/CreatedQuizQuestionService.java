package org.omniquiz.create.service;

import jakarta.transaction.Transactional;
import org.omniquiz.create.dto.CreatedQuizQuestionDTO;
import org.omniquiz.create.model.CreatedQuiz;
import org.omniquiz.create.model.CreatedQuizQuestion;
import org.omniquiz.create.repository.CreatedQuizQuestionRepo;
import org.omniquiz.create.repository.CreatedQuizRepo;
import org.omniquiz.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreatedQuizQuestionService {
    @Autowired
    private CreatedQuizRepo createdQuizRepo;

    @Transactional
    public String saveQuizQuestions(CreatedQuizQuestionDTO createdQuizQuestionDTO, User user) {
        CreatedQuiz createdQuiz = new CreatedQuiz();
        createdQuiz.setTitle(createdQuizQuestionDTO.getTitle());
        createdQuiz.setRefferal(createdQuizQuestionDTO.getRefferal());
        createdQuiz.setUser(user);
        createdQuiz.setCreatedAt(LocalDateTime.now());
        createdQuiz = createdQuizRepo.save(createdQuiz);
        for( CreatedQuizQuestionDTO.QuestionDTO question: createdQuizQuestionDTO.getQuestions()) {
            CreatedQuizQuestion createdQuizQuestion = new CreatedQuizQuestion();
            createdQuizQuestion.setQuestion(question.getQuestion());
            createdQuizQuestion.setCode(question.getCode());
            createdQuizQuestion.setExplanation(question.getExplanation());
            createdQuizQuestion.setOptions(question.getOptions());
            createdQuizQuestion.setCorrectIndex(question.getCorrectIndex());
            createdQuizQuestion.setQuiz(createdQuiz);
            createdQuizQuestion.setUser(user);
            createdQuiz.addQuestion(createdQuizQuestion);
        }
        createdQuizRepo.save(createdQuiz);
        return "ok";
    }
}
