package be.restiau.taskboardbackend.dal;

import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByOwnerId(Long ownerId);

    Optional<Board> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByIdAndOwnerId(Long boardId, Long ownerId);

    boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String newName);
}
