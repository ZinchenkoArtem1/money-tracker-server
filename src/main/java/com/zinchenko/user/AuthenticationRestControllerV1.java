package com.zinchenko.user;

import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.common.error.GenericException;
import com.zinchenko.user.dto.AuthenticationRequest;
import com.zinchenko.user.dto.AuthenticationResponse;
import com.zinchenko.user.dto.RegistrationRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationRestControllerV1.class);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicErrorResponse> handleException(Exception ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.internalServerError().body(
                new BasicErrorResponse("Internal server error")
        );
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<BasicErrorResponse> handleGenericException(GenericException ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.status(ex.getHttpStatus()).body(
                new BasicErrorResponse(ex.getMessage())
        );
    }
}
