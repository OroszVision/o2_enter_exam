package enter.task.o2_enter_exam.serivce;

import enter.task.o2_enter_exam.util.JwtUtil;
import enter.task.o2_enter_exam.dto.AuthRequest;
import enter.task.o2_enter_exam.dto.AuthResponse;
import enter.task.o2_enter_exam.dto.RegisterRequest;
import enter.task.o2_enter_exam.dto.RegisterResponse;
import enter.task.o2_enter_exam.model.AppUser;
import enter.task.o2_enter_exam.model.JwtToken;
import enter.task.o2_enter_exam.repository.AppUserRepository;
import enter.task.o2_enter_exam.repository.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final JwtTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        try {
            AppUser newUser = new AppUser();
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setEmail(request.getEmail());
            newUser.setUsername(request.getUsername());
            newUser.setFirstName(request.getFirstname());
            newUser.setLastName(request.getLastname());
            newUser.setAge(request.getAge());
            appUserRepository.save(newUser);
            return new RegisterResponse("User registered successfully!", newUser.getEmail());
        } catch (InvalidParameterException ex) {
            throw new InvalidParameterException("Your Inputted data were invalid: " + ex.getMessage());
        }
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            // 1. Load the user from the database using the username
            AppUser appUser = appUserRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Verify the password with BCryptPasswordEncoder
            if (!passwordEncoder.matches(request.getPassword(), appUser.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }

            // 3. Generate the JWT token
            String token = jwtUtil.generateToken(request.getUsername());

            // 4. Check for existing tokens and revoke if necessary
            Optional<JwtToken> existingTokenOpt = tokenRepository.findByUser(appUser);
            if (existingTokenOpt.isPresent()) {
                JwtToken existingToken = existingTokenOpt.get();
                if (!existingToken.isExpired()) {
                    existingToken.setExpired(true);
                    existingToken.setRevoked(true);
                    tokenRepository.save(existingToken);
                }
            }

            // 5. Save the new token in the database
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(token);
            jwtToken.setExpired(false);
            jwtToken.setRevoked(false);
            jwtToken.setUser(appUser);
            tokenRepository.save(jwtToken);

            // 6. Return response with the token
            return new AuthResponse("Authentication successful!", token);
        } catch (RuntimeException e) {
            // Return error if login fails
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }



    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return; // If token is not present, do nothing
        }

        jwt = authHeader.substring(7); // Remove "Bearer " part

        System.out.println("JWT Token: " + jwt); // Log the token for debugging

        var storedToken = tokenRepository.findByToken(jwt).orElse(null);

        if (storedToken != null) {
            // Set the token as expired and revoked
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            // Save changes to the repository
            tokenRepository.save(storedToken);

            // Clear the security context
            SecurityContextHolder.clearContext();
            System.out.println("User logged out successfully.");
        } else {
            System.out.println("Token not found in the repository.");
        }
    }

    public UserDetails loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
