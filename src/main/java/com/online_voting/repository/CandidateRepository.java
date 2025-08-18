package com.online_voting.repository;

import com.online_voting.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    //Optional<Candidate> findByCandidateName(String candidateName);
    Optional<Candidate> findByEmail(String email);



    @Query("{ '_id' : ?0 }")
    @Update("{ '$inc' : { 'votes' : 1 } }")
    void incrementVotes(String candidateId);



}
