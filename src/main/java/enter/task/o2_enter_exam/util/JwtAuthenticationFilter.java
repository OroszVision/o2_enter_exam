package enter.task.o2_enter_exam.util;

import enter.task.o2_enter_exam.repository.JwtTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                String username = jwtUtil.extractUsername(jwt);
                validateTokenAndSetAuthentication(jwt, username, request);
            } catch (ExpiredJwtException e) {
                System.out.println("Token has expired: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                return;
            } catch (Exception e) {
                System.out.println("Invalid token: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    private void validateTokenAndSetAuthentication(String jwt, String username, HttpServletRequest request) {
        // If the user exists and is not authenticated yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                var storedToken = tokenRepository.findByToken(jwt).orElse(null);

                if (storedToken != null && !storedToken.isExpired() && !storedToken.isRevoked()) {
                    // Ensure the token is associated with the correct user
                    if (storedToken.getUser().getUsername().equals(username)) {
                        // Create an Authentication object and set it in the SecurityContext
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("Token is valid and user is authenticated.");
                    } else {
                        System.out.println("User in token does not match the extracted username.");
                        throw new SecurityException("Token user does not match the authenticated user");
                    }
                } else {
                    System.out.println("Token is either expired or revoked.");
                    throw new SecurityException("Token is either expired or revoked");
                }
            } else {
                System.out.println("Invalid token.");
                throw new SecurityException("Invalid token");
            }
        }
    }
}
