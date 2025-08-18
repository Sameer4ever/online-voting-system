package com.online_voting.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidates")
@Data
public class Candidate {
    @Id
    private String candidateId;
    private String candidateName;
    private String email;
    private String password;
    private String role;
    private int votes = 0;
}
