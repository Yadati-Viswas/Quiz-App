package org.omniquiz.create.model;

import jakarta.persistence.*;
import lombok.Data;
import org.omniquiz.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="created_quiz")
public class CreatedQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    private String refferal;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreatedQuizQuestion> questions = new ArrayList<>();

    public void addQuestion(CreatedQuizQuestion question) {
        questions.add(question);
        question.setQuiz(this);
    }
}
