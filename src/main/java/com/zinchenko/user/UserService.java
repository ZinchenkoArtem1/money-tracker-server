package com.zinchenko.user;

import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.dto.RegistrationRequest;
import com.zinchenko.user.model.Role;
import com.zinchenko.user.model.User;
import com.zinchenko.user.model.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityUserService securityUserService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, SecurityUserService securityUserService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.securityUserService = securityUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email [%s] not found".formatted(email)));
    }

    public User getActiveUser() {
        String email = securityUserService.getActiveUser().getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email [%s] not found".formatted(email)));
    }

    public void create(RegistrationRequest registrationRequest) {
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
