package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.AuthResponseDTO;
import com.bridgelabz.quantitymeasurement.dto.LoginRequestDTO;
import com.bridgelabz.quantitymeasurement.dto.RegisterRequestDTO;
import com.bridgelabz.quantitymeasurement.security.JwtUtil;
import com.bridgelabz.quantitymeasurement.user.AuthProvider;
import com.bridgelabz.quantitymeasurement.user.UserEntity;
import com.bridgelabz.quantitymeasurement.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Local + Google OAuth2 Auth endpoints")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new local user")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider(AuthProvider.LOCAL);
        userService.saveUser(user);

        String token = jwtUtil.generateTokenFromEmail(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthResponseDTO(token, user.getEmail(), user.getName(),
                        user.getRole().name(), user.getImageUrl())
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password — returns JWT")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.ok(
                new AuthResponseDTO(token, user.getEmail(), user.getName(),
                        user.getRole().name(), user.getImageUrl())
        );
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<AuthResponseDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(
                new AuthResponseDTO(null, user.getEmail(), user.getName(),
                        user.getRole().name(), user.getImageUrl())
        );
    }
}
