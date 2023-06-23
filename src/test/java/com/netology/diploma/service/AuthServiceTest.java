package com.netology.diploma.service;

import com.netology.diploma.dto.auth.AuthRequest;
import com.netology.diploma.entity.User;
import com.netology.diploma.repository.UserRepository;
import com.netology.diploma.util.TokenStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenStorage tokenStorage;

    @Test
    void login() throws NoSuchAlgorithmException, InvalidKeySpecException {
        HttpServletRequest http = Mockito.mock(HttpServletRequest.class);
        when(http.getSession(true)).thenReturn(new MockHttpSession());
        AuthRequest request = new AuthRequest("admin", "thisismyadminpassword");
        User user = new User();
        user.setLogin("admin");
        user.setPassword("");
        user.setActive(true);

        when(userRepository.getByLogin(request)).thenReturn(user);
        String token = authService.login(http, request);

        assertNotNull(token);
    }
}
