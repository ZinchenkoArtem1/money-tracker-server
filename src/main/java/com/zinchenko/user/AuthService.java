package com.zinchenko.user;

import com.zinchenko.common.error.GenericException;
import com.zinchenko.security.jwt.JwtTokenService;
import com.zinchenko.user.domain.User;
import com.zinchenko.user.dto.AuthenticationRequest;
import com.zinchenko.user.dto.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService,
                       UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    public AuthenticationResponse auth(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new GenericException("Невірний емейль чи пароль");
        }

        User user = userService.getUserByEmail(request.getEmail());
        String token = jwtTokenService.generateToken(request.getEmail(), user.getRole().name());

        return new AuthenticationResponse()
                .setEmail(request.getEmail())
                .setToken(token);
    }
}
