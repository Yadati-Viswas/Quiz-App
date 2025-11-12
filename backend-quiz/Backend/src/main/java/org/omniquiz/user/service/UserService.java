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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordBcyrpt = new BCryptPasswordEncoder();

    public UserService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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

    public User login(LoginRequestDTO request){
        if (request.getIdentifier() == null || request.getPassword() == null) {
            return null;
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentifier(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier())
                    .orElseThrow();
            return user;

        } catch (Exception e) {
            return null;
        }
    }
}
