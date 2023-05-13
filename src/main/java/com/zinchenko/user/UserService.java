package com.zinchenko.user;

import com.zinchenko.user.dto.AuthenticationRequest;
import com.zinchenko.user.dto.AuthenticationResponse;
import com.zinchenko.user.dto.RegistrationRequest;
import com.zinchenko.security.jwt.JwtTokenService;
import com.zinchenko.user.model.Role;
import com.zinchenko.user.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
                               JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email [%s] not found".formatted(email)));
    }

    public AuthenticationResponse auth(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = getUserByEmail(request.getEmail());
        String token = jwtTokenService.createToken(request.getEmail(), user.getRole().name());

        return new AuthenticationResponse()
                .setEmail(request.getEmail())
                .setToken(token);
    }

    public void register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmailOrPassword(registrationRequest.getEmail(), registrationRequest.getPassword())) {
            throw new IllegalStateException("User with this email or password already exist in system");
        } else {
            userRepository.save(
                    new User()
                            .setEmail(registrationRequest.getEmail())
                            .setPassword(passwordEncoder.encode(registrationRequest.getPassword()))
                            .setFirstName(registrationRequest.getFirstName())
                            .setLastName(registrationRequest.getLastName())
                            .setRole(Role.USER)
            );
        }
    }
}
