package be.restiau.taskboardbackend.infra.mapper;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.api.board.dto.CreateBoardRequest;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.User;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public Board toEntity (String name, User owner) {
        Board board = new Board();
        board.setName(name);
        board.setOwner(owner);
        return board;
    }

    public BoardResponse toDTO (Board board) {
        return new BoardResponse (
                board.getId(),
                board.getName(),
                board.getCreatedAt()
        );
    }

}
