package enter.task.o2_enter_exam.controller;

import enter.task.o2_enter_exam.dto.AuthRequest;
import enter.task.o2_enter_exam.dto.AuthResponse;
import enter.task.o2_enter_exam.dto.RegisterRequest;
import enter.task.o2_enter_exam.dto.RegisterResponse;
import enter.task.o2_enter_exam.serivce.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8080"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            RegisterResponse registrationResponse = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
        } catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Registration failed: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponse("Registration failed: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponse("Registration failed due to an unexpected error: " + e.getMessage(), null));
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
            AuthResponse authenticationResponse = authService.authenticate(request);
            return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.logout(request, response, null);
            return ResponseEntity.ok("Logout successful");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

