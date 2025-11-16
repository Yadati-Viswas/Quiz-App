package org.omniquiz.generatequiz.repository;

import org.omniquiz.generatequiz.model.GeneratedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedQuizRepository extends JpaRepository<GeneratedQuiz, Long> {
}
