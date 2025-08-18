package com.online_voting.controller;

import com.online_voting.model.Candidate;
import com.online_voting.model.User;
import com.online_voting.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Tag(name = "3. Voting Operations",
        description = "Voter actions (Requires ROLE_VOTER)")
public class UserController {

    @Autowired
    private UserService userService;

    //Vote Candidate using CandidateID
    @PostMapping("/vote/{candidateId}")
    public ResponseEntity<String> voteForCandidate(@PathVariable String candidateId,
                                                   Authentication authentication) {
        String email = authentication.getName(); // comes from JWT
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Candidate votedCandidate = userService.voteForCandidate(user.getUserId(), candidateId);
            return ResponseEntity.ok(
                    "Vote successful! Total votes for " +
                            votedCandidate.getCandidateName() + ": " +
                            votedCandidate.getVotes()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
