package org.omniquiz.user.dto;

import lombok.Data;
import org.omniquiz.user.model.User;

@Data
public class LoginResponseDTO {
    private boolean success;
    private String message;
    private User user;

    public LoginResponseDTO(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
