package be.restiau.taskboardbackend.dal;

import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwner(User owner);
    Boolean existsByOwnerIdAndName(Long ownerId, String name);
}
