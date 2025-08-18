package com.online_voting.service;

import com.online_voting.model.Candidate;
import com.online_voting.model.User;
import com.online_voting.repository.CandidateRepository;
import com.online_voting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a new candidate
    public Candidate addCandidate(Candidate candidate) {
        candidate.setVotes(0); // default votes to 0
        return candidateRepository.save(candidate);
    }

    // Get all candidates
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Get candidate by ID
    public Optional<Candidate> getCandidateById(String id) {
        return candidateRepository.findById(id);
    }

    // Delete candidate by ID
    public void deleteCandidate(String id) {
        candidateRepository.deleteById(id);
    }



    public Candidate voteForCandidate(String candidateId, String userEmail) {
        // 1. Find user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check if user has already voted
        if (user.isHasVoted()) {
            throw new IllegalStateException("User already voted!");
        }

        // 3. Increment candidate votes atomically
        candidateRepository.incrementVotes(candidateId);

        // 4. Mark user as voted
        user.setHasVoted(true);
        userRepository.save(user);

        // 5. Return updated candidate
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }




    public Optional<Candidate> getCandidateByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }


    public int getVoteCount(String candidateId) {
        return candidateRepository.findById(candidateId)
                .map(Candidate::getVotes)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

}
