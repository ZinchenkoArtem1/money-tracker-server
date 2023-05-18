package com.zinchenko.user;

import com.zinchenko.user.dto.AuthenticationRequest;
import com.zinchenko.user.dto.AuthenticationResponse;
import com.zinchenko.user.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private final UserService userService;
    private final AuthService authService;

    public AuthenticationRestControllerV1(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest request) {
        userService.create(request);
        return ResponseEntity.ok().build();
    }
}
