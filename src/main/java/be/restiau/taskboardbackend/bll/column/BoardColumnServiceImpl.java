package be.restiau.taskboardbackend.bll.column;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.dal.BoardColumnRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import be.restiau.taskboardbackend.infra.mapper.BoardColumnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
