package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @PostMapping("/login")
    public Object login(
            @RequestParam(value = "user", required = false) String user,
            @RequestParam(value = "encryptedPass", required = false) String encryptedPass,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request) throws Exception {

        String uname = user != null ? user : username;
        String pwd = encryptedPass != null ? encryptedPass : password;

        if (uname == null || pwd == null) {
            throw new RuntimeException("Missing credentials");
        }

        // authenticate via AuthenticationManager to establish server-side SecurityContext (create session)
        try {
            AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(uname, pwd);
            Authentication auth = authManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
            // ensure HTTP session is created so Thymeleaf sec:authorize can read SecurityContext
            request.getSession(true);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(uname);
        return ResponseEntity.ok(token);
    }

}
