package org.omniquiz.user.dto;

import lombok.Data;
import org.omniquiz.user.model.User;

@Data
public class LoginResponseDTO {
    private boolean success;
    private String token;
    private long expiresIn;
    private User user;

    public LoginResponseDTO(boolean success, String token, long expiresIn, User user) {
        this.success = success;
        this.token = token;
        this.expiresIn=expiresIn;
        this.user = user;
    }
}
