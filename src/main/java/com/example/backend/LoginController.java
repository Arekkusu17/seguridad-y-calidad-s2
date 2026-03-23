package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

@Controller
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

        // authenticate using AuthenticationManager so we establish a session for form logins
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(uname, pwd));
        SecurityContextHolder.getContext().setAuthentication(auth);
        // Persist the security context in the HTTP session so subsequent requests using JSESSIONID are authenticated
        request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        String token = jwtAuthtenticationConfig.getJWTToken(uname);

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("text/html")) {
            // browser form submit -> redirect to patients page
            return "redirect:/patients";
        }

        // API client (fetch) -> return token string
        return ResponseEntity.ok(token);
    }

}
