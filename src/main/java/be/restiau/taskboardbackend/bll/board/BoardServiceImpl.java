package be.restiau.taskboardbackend.bll.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Override
    @Transactional
    public BoardResponse create(User owner, String name) {
        if (boardRepository.existsByOwnerIdAndName(owner.getId(), name)) {
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
}
