package be.restiau.taskboardbackend.api.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameRequest(
        @NotBlank @Size(max = 120)
        String newName
) {
}
