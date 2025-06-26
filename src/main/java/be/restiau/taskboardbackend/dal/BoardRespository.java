package be.restiau.taskboardbackend.dal;

import be.restiau.taskboardbackend.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRespository extends JpaRepository<Board, Long> {
    List<Board> findByOwnerId(Long ownerId);
}
