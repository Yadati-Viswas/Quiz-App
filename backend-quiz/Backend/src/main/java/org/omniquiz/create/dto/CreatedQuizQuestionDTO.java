package org.omniquiz.create.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreatedQuizQuestionDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title too long")
    private String title;

    private String refferal; // optional

    @NotEmpty(message = "At least one question is required")
    private List<QuestionDTO> questions;

    @Data
    public static class QuestionDTO {
        @NotBlank
        private String question;

        private String code; // optional

        @NotBlank
        private String explanation;

        @NotEmpty
        @Size(min = 2, max = 6, message = "Options must be 2–6")
        private List<String> options;

        @NotBlank
        private Integer correctIndex; // e.g., "0", "1", etc.
    }
}
