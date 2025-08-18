package com.online_voting.config;

import com.online_voting.model.User;
import com.online_voting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@admin.com";  // predefined admin email
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setName("Administrator");
            admin.setPassword(passwordEncoder.encode("Admin@123")); // predefined strong password
            admin.setRole("ROLE_ADMIN");
            admin.setHasVoted(false);

            userRepository.save(admin);
            System.out.println("Predefined admin created: " + adminEmail);
        } else {
            System.out.println("Admin already exists: " + adminEmail);
        }
    }
}
