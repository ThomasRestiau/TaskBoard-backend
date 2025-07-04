package be.restiau.taskboardbackend.bll.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Override
    public BoardResponse create(User owner, String name) {
        if (boardRepository.existsByOwnerIdAndNameIgnoreCase(owner.getId(), name)) {
            throw new IllegalStateException("Board name already used");
            // TODO BoardAlreadyExistsException
        }
        Board board = boardMapper.toEntity(name, owner);

        board.addColumn(new BoardColumn("Todo", 0));
        board.addColumn(new BoardColumn("Doing", 1));
        board.addColumn(new BoardColumn("Done", 2));

        boardRepository.save(board);

        return boardMapper.toDTO(board);
    }

    @Override
    public BoardResponse rename(Long boardId, Long ownerId, String newName) {
        Board board = boardRepository.findByIdAndOwnerId(boardId, ownerId)
                .orElseThrow(() -> new AccessDeniedException("Not owner or board not found"));

        if (boardRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, newName.trim())) {
            throw new IllegalStateException("Board name already used");
        }

        board.setName(newName.trim());

        return boardMapper.toDTO(board);
    }

    @Override
    public void delete(Long boardId, Long ownerId) {
        if (!boardRepository.existsByIdAndOwnerId(boardId, ownerId)) {
            throw new AccessDeniedException("Not owner or board not found");
        }
        boardRepository.deleteById(boardId);
    }
}
