package be.restiau.taskboardbackend.bll.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.domain.User;

public interface BoardService {

    BoardResponse create(User owner, String name);

}
