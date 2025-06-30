package be.restiau.taskboardbackend.api.user;

import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;
import be.restiau.taskboardbackend.infra.mapper.UserMapper;
import be.restiau.taskboardbackend.infra.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserLiteDTO> me(@AuthenticationPrincipal AppUserDetails principal) {
        return ResponseEntity.ok(userMapper.toDTO(principal.getUser()));
    }
}
