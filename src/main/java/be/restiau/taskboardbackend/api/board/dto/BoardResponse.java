package be.restiau.taskboardbackend.api.board.dto;

import java.time.Instant;

public record BoardResponse(
        Long id,
        String name,
        Instant createdAt
) {
}
