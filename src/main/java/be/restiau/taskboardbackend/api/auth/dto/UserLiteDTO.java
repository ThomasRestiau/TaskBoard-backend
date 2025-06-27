package be.restiau.taskboardbackend.api.auth.dto;

public record UserLiteDTO (
        Long id,
        String email,
        String fullName
) {
}
