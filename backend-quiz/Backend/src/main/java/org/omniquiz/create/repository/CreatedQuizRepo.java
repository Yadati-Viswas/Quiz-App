package org.omniquiz.create.repository;

import org.omniquiz.create.model.CreatedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatedQuizRepo extends JpaRepository<CreatedQuiz,Long> {
}
