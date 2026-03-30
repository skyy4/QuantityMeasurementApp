package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.AuthResponseDTO;
import com.bridgelabz.quantitymeasurement.security.JwtUtil;
import com.bridgelabz.quantitymeasurement.user.UserEntity;
import com.bridgelabz.quantitymeasurement.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@Tag(name = "OAuth2", description = "Google OAuth2 redirect handler")
public class OAuth2RedirectController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * This endpoint is the landing page after Google OAuth2 login.
     * Spring's OAuth2SuccessHandler redirects here with ?token=<JWT>.
     * It validates the token and returns the user's profile as JSON.
     *
     * Browser flow:
     *   GET /oauth2/authorization/google
     *   → Google Login
     *   → GET /oauth2/redirect?token=eyJ...   ← THIS endpoint
     *   → JSON response with token + user info
     */
    @GetMapping("/redirect")
    @Operation(summary = "OAuth2 redirect landing — receives token after Google login")
    public ResponseEntity<AuthResponseDTO> handleOAuth2Redirect(@RequestParam String token) {
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }

        String email = jwtUtil.getEmailFromToken(token);
        UserEntity user = userService.findByEmail(email);

        return ResponseEntity.ok(
                new AuthResponseDTO(
                        token,
                        user.getEmail(),
                        user.getName(),
                        user.getRole().name(),
                        user.getImageUrl()
                )
        );
    }
}
