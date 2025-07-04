package be.restiau.taskboardbackend.api.board.dto;

public record ColumnResponse(
        Long id,
        String Name,
        Integer Position
) {
}
