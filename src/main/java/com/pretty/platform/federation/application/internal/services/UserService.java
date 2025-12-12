package com.pretty.platform.federation.application.internal.services;

import com.pretty.platform.federation.domain.model.aggregates.User;
import com.pretty.platform.federation.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String googleId = oAuth2User.getAttribute("sub");
        String name = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        return userRepository.findByGoogleId(googleId)
                .map(existingUser -> {
                    existingUser.setName(name);
                    existingUser.setEmail(email);
                    existingUser.setPictureUrl(pictureUrl);
                    existingUser.setEmailVerified(emailVerified);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User(name, email, googleId, pictureUrl, emailVerified);
                    return userRepository.save(newUser);
                });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
