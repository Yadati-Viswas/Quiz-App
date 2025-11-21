package org.omniquiz.create.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.omniquiz.create.model.CreatedQuizQuestion;

public interface CreatedQuizQuestionRepo extends JpaRepository<CreatedQuizQuestion,Long> {
}
