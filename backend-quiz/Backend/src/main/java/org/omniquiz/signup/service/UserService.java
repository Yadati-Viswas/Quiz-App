package org.omniquiz.signup.service;

import org.omniquiz.signup.dto.SignupRequestDTO;
import org.omniquiz.signup.dto.SignupResponseDTO;
import org.omniquiz.signup.model.User;
import org.omniquiz.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
