package com.online_voting.auth;

import com.online_voting.model.Candidate;
import com.online_voting.model.User;
import com.online_voting.repository.CandidateRepository;
import com.online_voting.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "1. Authentication", description = "User login endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key jwtSecret;

    @PostConstruct
    public void init() {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    @Operation(
            summary = "User Login",
            description = "Authenticate user and return JWT token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // 1️⃣ Try finding User by email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            String token = generateToken(user.getEmail(), user.getRole(), user.getUserId());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "userId", user.getUserId(),
                    "expiresIn", jwtExpirationMs
            ));
        }

        // 2️⃣ Try finding Candidate by email
        Optional<Candidate> candidateOpt = candidateRepository.findByEmail(request.getEmail());
        if (candidateOpt.isPresent()) {
            Candidate candidate = candidateOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            String token = generateToken(candidate.getEmail(), candidate.getRole(), candidate.getCandidateId());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", candidate.getRole(),
                    "candidateId", candidate.getCandidateId(),
                    "expiresIn", jwtExpirationMs
            ));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    private String generateToken(String email, String role, String id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact();
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}