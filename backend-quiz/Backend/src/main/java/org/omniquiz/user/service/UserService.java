package org.omniquiz.user.service;

import org.omniquiz.user.dto.LoginRequestDTO;
import org.omniquiz.user.dto.LoginResponseDTO;
import org.omniquiz.user.dto.SignupRequestDTO;
import org.omniquiz.user.dto.SignupResponseDTO;
import org.omniquiz.user.model.User;
import org.omniquiz.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder passwordBcyrpt = new BCryptPasswordEncoder();

    public SignupResponseDTO signup(SignupRequestDTO request) {
        // Validate mandatory fields
        if (request.getUsername() == null || request.getEmail() == null ||
                request.getPassword() == null || request.getConfirmPassword() == null) {
            return new SignupResponseDTO(false, "All mandatory fields (username, email, password, confirm password) are required", null);
        }

        // Check for existing username or email
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new SignupResponseDTO(false, "Username already exists", null);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new SignupResponseDTO(false, "Email already exists", null);
        }

        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new SignupResponseDTO(false, "Passwords do not match", null);
        }

        // Create new user
        User user = new User(
                request.getLastName(),
                request.getFirstName(),
                request.getUsername(),
                request.getEmail(),
                request.getPhone(),
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);
        return new SignupResponseDTO(true, "Signup successful", savedUser.getId());
    }

    public LoginResponseDTO login(LoginRequestDTO request){
        if(request.getIdentifier()==null || request.getPassword()==null){
            return new LoginResponseDTO(false, "All mandatory fields are required", null);
        }
        User user = userRepository.findByUsernameOrEmail(request.getIdentifier(),request.getIdentifier())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (validateLogin(request.getIdentifier(), request.getPassword())) {
            return new LoginResponseDTO(true, "Login successful", user);
        }
        return new LoginResponseDTO(false, "Invalid credentials", null);
    }

    public boolean validateLogin(String identifier, String plainPassword) {
        // Fetch user by username or email
        User user = userRepository.findByUsernameOrEmail(identifier,identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Compare the plain password with the hashed password
        return passwordBcyrpt.matches(plainPassword, user.getPassword());
    }
}
