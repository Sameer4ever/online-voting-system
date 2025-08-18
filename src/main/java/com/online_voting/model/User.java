package com.online_voting.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
@Data
@Schema(description = "System user with voting privileges")
public class User {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String userId; // MongoDB ObjectId as String

    @Schema(description = "Unique email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's full name", example = "John Doe")
    private String name;

    private String password;

    private int phone;

    // Hidden fields
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean hasVoted;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String role;
}
