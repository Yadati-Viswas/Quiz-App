package org.omniquiz.signup.dto;

import lombok.Data;

@Data
public class SignupResponseDTO {
    private boolean success;
    private String message;
    private Long userId;

    public SignupResponseDTO(boolean success, String message, Long userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }
}
