package be.restiau.taskboardbackend.bll.auth;

import be.restiau.taskboardbackend.api.auth.dto.SignupRequest;
import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;
import be.restiau.taskboardbackend.dal.UserRepository;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.UserMapper;
import be.restiau.taskboardbackend.infra.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserLiteDTO register(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.email())){
            throw new IllegalStateException("Email already exists");
            // TODO EmailAlreadyExistException
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.password());

        User toSave = userMapper.toEntity(signupRequest, hashedPassword);
        User savedUser = userRepository.save(toSave);

        return userMapper.toDTO(savedUser);
    }
}
