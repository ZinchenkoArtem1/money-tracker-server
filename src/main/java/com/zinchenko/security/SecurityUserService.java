package com.zinchenko.security;

import com.zinchenko.user.domain.UserRepository;
import com.zinchenko.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalStateException("User with email [%s] not exists".formatted(email)));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getRole().getAuthorities()
        );
    }

    public UserDetails getActiveUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
