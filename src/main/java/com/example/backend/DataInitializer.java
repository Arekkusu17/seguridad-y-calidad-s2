package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.backend.model.User;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private com.example.backend.repository.UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User u = new User();
            u.setUsername("admin");
            u.setEmail("admin@example.com");
            u.setPassword(passwordEncoder.encode("adminpass"));
            u.setRoles("ROLE_ADMIN");
            userRepository.save(u);
        }

        if (userRepository.findByUsername("vet") == null) {
            User v = new User();
            v.setUsername("vet");
            v.setEmail("vet@example.com");
            v.setPassword(passwordEncoder.encode("vetpass"));
            v.setRoles("ROLE_VET");
            userRepository.save(v);
        }

        if (userRepository.findByUsername("cal") == null) {
            User c = new User();
            c.setUsername("cal");
            c.setEmail("cal@example.com");
            c.setPassword(passwordEncoder.encode("userpass"));
            c.setRoles("ROLE_USER");
            userRepository.save(c);
        }
    }
}
