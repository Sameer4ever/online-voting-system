package com.online_voting.controller;

import com.online_voting.model.Candidate;
import com.online_voting.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/votes")
    public ResponseEntity<?> getVoteCount(Authentication authentication) {
        try {
            String candidateEmail = authentication.getName(); // get email from token
            Candidate candidate = candidateService.getCandidateByEmail(candidateEmail)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            return ResponseEntity.ok(Map.of(
                    "candidateId", candidate.getCandidateId(),
                    "totalVotes", candidate.getVotes()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}