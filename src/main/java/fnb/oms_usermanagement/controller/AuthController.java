package fnb.oms_usermanagement.controller;

import fnb.oms_usermanagement.dto.AuthResponse;
import fnb.oms_usermanagement.dto.LoginRequest;
import fnb.oms_usermanagement.dto.RegisterRequest;
import fnb.oms_usermanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // This exposes a POST endpoint at http://localhost:8081/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // This exposes a POST endpoint at http://localhost:8081/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
