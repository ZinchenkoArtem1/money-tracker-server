package com.zinchenko.user;

import com.zinchenko.security.jwt.JwtTokenService;
import com.zinchenko.user.dto.AuthenticationRequest;
import com.zinchenko.user.dto.AuthenticationResponse;
import com.zinchenko.user.model.Role;
import com.zinchenko.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

//    private AuthService authService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//    @Mock
//    private JwtTokenService jwtTokenService;
//    @Mock
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        authService = new AuthService(authenticationManager, jwtTokenService, userService);
//    }
//
//    @Test
//    void authTest() {
//        String token = UUID.randomUUID().toString();
//        String email = UUID.randomUUID().toString();
//        String password = UUID.randomUUID().toString();
//        AuthenticationRequest request = new AuthenticationRequest()
//                .setEmail(email)
//                .setPassword(password);
//
//        when(userService.getUserByEmail(email)).thenReturn(new User().setRole(Role.USER));
//        when(jwtTokenService.createToken(email, Role.USER.name())).thenReturn(token);
//
//        AuthenticationResponse response = authService.auth(request);
//
//        verify(authenticationManager).authenticate(argThat(a ->
//                        a.getPrincipal().equals(email) &&
//                                a.getCredentials().equals(password)
//                )
//        );
//        assertEquals(email, response.getEmail());
//        assertEquals(token, response.getToken());
//    }
}
