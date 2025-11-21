package org.omniquiz.create.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="created_quiz_question")
public class CreatedQuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @ElementCollection
    @CollectionTable(name = "created_quiz_question_option", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    @OrderColumn(name = "option_index")
    private List<String> options;

    @Column(name = "correct_index")
    private Integer correctIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_quiz_id", nullable = false)
    private CreatedQuiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private org.omniquiz.user.model.User user;
}
