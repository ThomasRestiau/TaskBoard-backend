package be.restiau.taskboardbackend.bll.column;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;
import be.restiau.taskboardbackend.api.board.dto.ReorderColumnRequest;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.dal.BoardColumnRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import be.restiau.taskboardbackend.infra.mapper.BoardColumnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnMapper boardColumnMapper;

    @Override
    public ColumnResponse addColumn(Long boardId, Long ownerId, String name) {
        Board board = boardRepository.findByIdAndOwnerId(boardId, ownerId)
                .orElseThrow(() -> new AccessDeniedException("Not owner or board not found"));

        if (boardColumnRepository.existsByBoardIdAndNameIgnoreCase(boardId, name)) {
            throw new IllegalArgumentException("Column name already exists");
        }

        Integer position = boardColumnRepository.countByBoardId(boardId);
        BoardColumn boardColumn = new BoardColumn(name, position);
        board.addColumn(boardColumn);

        return boardColumnMapper.toDTO(boardColumn);
    }

    @Override
    public void reorderColumns(Long boardId, Long ownerId, List<ReorderColumnRequest> payload) {

        Board board = boardRepository.findByIdAndOwnerId(boardId, ownerId)
                .orElseThrow(() -> new AccessDeniedException("Not owner or board not found"));

        List<BoardColumn> columns = boardColumnRepository.findByBoardIdOrderByPosition(boardId);

        Set<Long> existingIds = columns.stream().map(BoardColumn::getId).collect(Collectors.toSet());

        Set<Long> payloadIds = payload.stream().map(ReorderColumnRequest::columnId).collect(Collectors.toSet());

        Set<Integer> positions = payload.stream().map(ReorderColumnRequest::position).collect(Collectors.toSet());

        validatePayload(existingIds, payloadIds, positions, payload);

        Map<Long, Integer> newPositions = payload.stream()
                .collect(Collectors.toMap(ReorderColumnRequest::columnId, ReorderColumnRequest::position));

        for (BoardColumn column : columns) {
            Integer newPosition = newPositions.get(column.getId());
            if (newPosition != null) {
                column.setPosition(newPosition);
            }
        }

        boardColumnRepository.saveAll(columns);
    }

    private void validatePayload(Set<Long> existingIds, Set<Long> payloadIds, Set<Integer> positions, List<ReorderColumnRequest> payload) {
        if (!existingIds.equals(payloadIds)) {
            throw new IllegalArgumentException("Payload must contain exactly all column IDs of the board");
            // TODO InvalidColumnReorderException
        }

        if (positions.size() != payload.size()) {
            throw new IllegalArgumentException("Duplicate positions are not allowed");
            // TODO DuplicatePositionsNotAllowedException
        }
    }
}
