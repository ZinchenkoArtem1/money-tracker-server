package com.zinchenko.security;

import com.zinchenko.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class SecurityUserServiceTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private SecurityUserService securityUserService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        securityUserService = new SecurityUserService(userRepository);
    }

    @Test
    void loadUserByUsernameNotExistTest() {
        String email = UUID.randomUUID().toString();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> securityUserService.loadUserByUsername(email));
        assertEquals("User with email [%s] not exists".formatted(email), exc.getMessage());
    }

    @Test
    void loadUserByUsernameSuccessTest() {
        String email = UUID.randomUUID().toString();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> securityUserService.loadUserByUsername(email));
        assertEquals("User with email [%s] not exists".formatted(email), exc.getMessage());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void getActiveUserTest() {
        UserDetails userDetails = securityUserService.getActiveUser();

        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
    }
}
