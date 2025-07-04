package be.restiau.taskboardbackend.infra.mapper;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import org.springframework.stereotype.Component;

@Component
public class BoardColumnMapper {

    public BoardColumn toEntity(Board board, String columnName) {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setBoard(board);
        boardColumn.setName(columnName);
        return boardColumn;
    }

    public ColumnResponse toDTO(BoardColumn boardColumn) {
        return new ColumnResponse(
                boardColumn.getId(),
                boardColumn.getName(),
                boardColumn.getPosition()
        );
    }
}
