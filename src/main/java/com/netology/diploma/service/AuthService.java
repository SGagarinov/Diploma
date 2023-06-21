package com.netology.diploma.service;

import com.netology.diploma.dto.auth.AuthRequest;
import com.netology.diploma.repository.UserRepository;
import com.netology.diploma.util.TokenStorage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenStorage tokenStorage;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenStorage tokenStorage) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenStorage = tokenStorage;
    }

    public String login(AuthRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (userRepository.getByLogin(request)) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = UUID.randomUUID().toString();
            tokenStorage.getTokenList().put(request.getLogin(), token);
            return token;
        }
        return null;
    }
    public void logout(String authToken) {
        tokenStorage.getTokenList().remove(authToken);
    }
}
