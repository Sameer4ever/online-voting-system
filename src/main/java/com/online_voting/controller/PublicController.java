package com.online_voting.controller;

import com.online_voting.model.Candidate;
import com.online_voting.model.User;
import com.online_voting.service.CandidateService;
import com.online_voting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Tag(name = "2. Public APIs", description = "Endpoints available without authentication")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Operation(
            summary = "User Registration ",
            description = "Create a new voter account (role: ROLE_VOTER)"
    )
    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_VOTER");
        User savedUser = userService.addUser(user);
        return ResponseEntity.ok(savedUser);
    }


    @Operation(
            summary = "Candidate Registration",
            description = "Create a new candidate account (role: ROLE_CANDIDATE)"
    )
    @PostMapping("/candidate/register")
    public ResponseEntity<Candidate> registerCandidate(@RequestBody Candidate candidate) {
        candidate.setPassword(passwordEncoder.encode(candidate.getPassword()));
        candidate.setRole("ROLE_CANDIDATE");
        Candidate savedCandidate = candidateService.addCandidate(candidate);
        return ResponseEntity.ok(savedCandidate);
    }


    @Operation(
            summary = "All Candidates",
            description = "View all candidates and vote accordingly !! "
    )
    @GetMapping("/all-candidates")
    public ResponseEntity<List<Map<String, String>>> getCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        List<Map<String, String>> result = candidates.stream()
                .map(c -> Map.of(
                        "id", c.getCandidateId(),
                        "name", c.getCandidateName()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }
}