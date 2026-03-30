package com.bridgelabz.quantitymeasurement.security;

import com.bridgelabz.quantitymeasurement.user.AuthProvider;
import com.bridgelabz.quantitymeasurement.user.UserEntity;
import com.bridgelabz.quantitymeasurement.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String googleId = oAuth2User.getAttribute("sub");

        UserEntity user;
        if (userService.existsByEmail(email)) {
            user = userService.findByEmail(email);
            user.setName(name);
            user.setImageUrl(picture);
            userService.saveUser(user);
        } else {
            user = new UserEntity();
            user.setEmail(email);
            user.setName(name);
            user.setImageUrl(picture);
            user.setProvider(AuthProvider.GOOGLE);
            user.setProviderId(googleId);
            user.setEnabled(true);
            userService.saveUser(user);
        }

        String token = jwtUtil.generateTokenFromEmail(Objects.requireNonNull(email, "OAuth2 email attribute must not be null"));

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
