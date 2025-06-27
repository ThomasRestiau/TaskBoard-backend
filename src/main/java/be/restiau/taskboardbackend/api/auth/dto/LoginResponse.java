package be.restiau.taskboardbackend.api.auth.dto;

public record LoginResponse(
        String token,
        UserLiteDTO user
) {
}
