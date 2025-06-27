package be.restiau.taskboardbackend.infra.mapper;

import be.restiau.taskboardbackend.api.auth.dto.SignupRequest;
import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;
import be.restiau.taskboardbackend.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity (SignupRequest req, String encodedPassword) {
        User user = new User();
        user.setEmail(req.email());
        user.setFullName(req.fullName());
        user.setPasswordHash(encodedPassword);
        return user;
    }

    public UserLiteDTO toDTO (User entity) {
        return new UserLiteDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getFullName()
        );
    }
}
