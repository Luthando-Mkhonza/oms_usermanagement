package fnb.oms_usermanagement.service;

import fnb.oms_usermanagement.dto.AuthResponse;
import fnb.oms_usermanagement.dto.LoginRequest;
import fnb.oms_usermanagement.dto.RegisterRequest;
import fnb.oms_usermanagement.entity.Role;
import fnb.oms_usermanagement.entity.User;
import fnb.oms_usermanagement.entity.UserCredential;
import fnb.oms_usermanagement.repository.UserCredentialRepository;
import fnb.oms_usermanagement.repository.UserRepository;
import fnb.oms_usermanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // We use @Transactional because we are saving to TWO tables (users and user_credentials).
    // If one fails, we want the whole process to rollback so we don't get half-created users.
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // 2. Create the User profile
        User user = User.builder()
                .firstName(request.getFirstName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .role(Role.CUSTOMER) // Default role for new signups
                .build();
        User savedUser = userRepository.save(user);

        // 3. Create and save the User Credentials
        // IMPORTANT: We use passwordEncoder to scramble (hash) the password!
        UserCredential credential = UserCredential.builder()
                .user(savedUser)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        userCredentialRepository.save(credential);

        // 4. Generate a JWT token for the new user so they are instantly logged in
        String jwtToken = jwtService.generateToken(savedUser.getEmail(), savedUser.getId(), savedUser.getRole().name());

        return AuthResponse.builder()
                .token(jwtToken)
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Find the user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find their credentials using the user's ID
        UserCredential credential = userCredentialRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Credentials not found"));

        // 3. Check if the provided password matches the scrambled hash in the database
        if (!passwordEncoder.matches(request.getPassword(), credential.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // 4. Generate the JWT token
        String jwtToken = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Login successful")
                .build();
    }
}
