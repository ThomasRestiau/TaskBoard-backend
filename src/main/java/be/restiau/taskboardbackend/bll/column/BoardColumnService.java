package be.restiau.taskboardbackend.bll.column;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;
import be.restiau.taskboardbackend.api.board.dto.ReorderColumnRequest;

import java.util.List;

public interface BoardColumnService {
    ColumnResponse addColumn(Long boardId, Long ownerId, String name);
    void reorderColumns(Long boardId, Long ownerId, List<ReorderColumnRequest> payload);
}
