package be.restiau.taskboardbackend.dal;

import be.restiau.taskboardbackend.domain.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardIdOrderByPosition(Long boardId);
    boolean existsByBoardIdAndNameIgnoreCase(Long boardId, String name);
    Integer countByBoardId(Long boardId);
}
