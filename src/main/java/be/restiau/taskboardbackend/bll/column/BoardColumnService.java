package be.restiau.taskboardbackend.bll.column;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;

public interface BoardColumnService {
    ColumnResponse addColumn(Long boardId, Long ownerId, String name);
}
