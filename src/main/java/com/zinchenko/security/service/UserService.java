package com.zinchenko.security.service;

import com.zinchenko.security.dto.AuthenticationRequest;
import com.zinchenko.security.dto.AuthenticationResponse;
import com.zinchenko.security.dto.RegistrationRequest;
import com.zinchenko.security.model.Role;
import com.zinchenko.security.model.Status;
import com.zinchenko.security.model.User;
import com.zinchenko.security.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse auth(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());

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
                            .setStatus(Status.ACTIVE)
            );
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email [%s] not found".formatted(email)));
    }
}
