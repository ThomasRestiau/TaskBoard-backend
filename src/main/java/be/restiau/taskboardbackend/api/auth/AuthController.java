package be.restiau.taskboardbackend.api.auth;

import be.restiau.taskboardbackend.api.auth.dto.LoginRequest;
import be.restiau.taskboardbackend.api.auth.dto.LoginResponse;
import be.restiau.taskboardbackend.api.auth.dto.SignupRequest;
import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;
import be.restiau.taskboardbackend.bll.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserLiteDTO> signup(@Valid @RequestBody SignupRequest signupRequest) {
        UserLiteDTO dto = authService.register(signupRequest);
        return ResponseEntity.created(URI.create("/api/users")).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
