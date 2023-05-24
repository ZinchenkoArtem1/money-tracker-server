package com.zinchenko.user;

import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.dto.RegistrationRequest;
import com.zinchenko.user.model.User;
import com.zinchenko.user.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityUserService securityUserService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, securityUserService, passwordEncoder);
    }

    @Test
    void getUserByEmailSuccessTest() {
        User user = mock(User.class);
        String email = UUID.randomUUID().toString();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUserByEmail(email));
    }

    @Test
    void getUserByEmailFailedTest() {
        String email = UUID.randomUUID().toString();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> userService.getUserByEmail(email));
        assertEquals("User with email [%s] not found".formatted(email), exc.getMessage());
    }

    @Test
    void createUserWithEmailOrPasswordThatExistTest() {
        String email = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        RegistrationRequest registrationRequest = new RegistrationRequest()
                .setEmail(email)
                .setPassword(password);

        when(userRepository.existsByEmailOrPassword(email, password)).thenReturn(true);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> userService.create(registrationRequest));
        assertEquals("User with this email or password already exist in system", exc.getMessage());
    }

    @Test
    void createUserSuccessTest() {
        String email = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        String encodedPassword = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        RegistrationRequest registrationRequest = new RegistrationRequest()
                .setEmail(email)
                .setPassword(password)
                .setFirstName(firstName)
                .setLastName(lastName);

        when(userRepository.existsByEmailOrPassword(email, password)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        userService.create(registrationRequest);

        verify(userRepository).save(argThat(u ->
                u.getEmail().equals(email) &&
                        u.getPassword().equals(encodedPassword) &&
                        u.getFirstName().equals(firstName) &&
                        u.getLastName().equals(lastName)
        ));
    }

    @Test
    void getActiveUserSuccessTest() {
        UserDetails userDetails = mock(UserDetails.class);
        String email = UUID.randomUUID().toString();
        User user = mock(User.class);

        when(userDetails.getUsername()).thenReturn(email);
        when(securityUserService.getActiveUser()).thenReturn(userDetails);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getActiveUser());
    }

    @Test
    void getActiveUserFailedTest() {
        UserDetails userDetails = mock(UserDetails.class);
        String email = UUID.randomUUID().toString();

        when(userDetails.getUsername()).thenReturn(email);
        when(securityUserService.getActiveUser()).thenReturn(userDetails);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> userService.getActiveUser());
        assertEquals("User with email [%s] not found".formatted(email), exc.getMessage());
    }
}
