package org.omniquiz.generatequiz.repository;

import org.omniquiz.generatequiz.model.GeneratedQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedQuizQuestionRepository extends JpaRepository<GeneratedQuizQuestion,Long> {
}
