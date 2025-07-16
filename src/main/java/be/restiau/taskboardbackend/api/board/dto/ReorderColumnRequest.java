package be.restiau.taskboardbackend.api.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReorderColumnRequest(
        @NotNull
        Long columnId,
        @NotNull @Min(0)
        Integer position
) {
}
