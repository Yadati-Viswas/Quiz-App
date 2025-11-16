package org.omniquiz.generatequiz.model;

import jakarta.persistence.*;
import lombok.Data;
import org.omniquiz.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "generated_quiz")
@Data
public class GeneratedQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_title", nullable = false)
    private String title;

    @Column(name = "topic")
    private String topic;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @OneToMany(mappedBy = "generatedQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedQuizQuestion> questions = new ArrayList<>();

    // Helper methods
    public void addQuestion(GeneratedQuizQuestion question) {
        questions.add(question);
        question.setGeneratedQuiz(this);
    }

    public void removeQuestion(GeneratedQuizQuestion question) {
        questions.remove(question);
        question.setGeneratedQuiz(null);
    }
}