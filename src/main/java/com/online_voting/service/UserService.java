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
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    // Add a new user
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // Delete user by ID
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Admin cannot be deleted!");
        }

        userRepository.deleteById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Candidate voteForCandidate(String userId, String candidateId) {
        // 1. Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check if already voted
        if (user.isHasVoted()) {
            throw new RuntimeException("User already voted!");
        }

        // 3. Verify candidate exists
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // 4. Update records
        candidate.setVotes(candidate.getVotes() + 1);
        user.setHasVoted(true);

        candidateRepository.save(candidate);
        userRepository.save(user);

        return candidate;
    }
}
