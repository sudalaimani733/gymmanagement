package com.gym.gymmanagement.authcontroller;

import com.gym.gymmanagement.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String ADMIN_EMAIL = "mani123@gmail.com";
    private static final String ADMIN_PASSWORD = "mani123";

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", email,
                    "name", "Mani"
            ));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}