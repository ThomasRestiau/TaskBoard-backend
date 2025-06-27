package be.restiau.taskboardbackend.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest (
        @Email
        String email,
        @NotBlank
        String fullName,
        @Size(min = 8)
        String password
){
}
