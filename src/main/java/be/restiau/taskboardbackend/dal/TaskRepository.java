package be.restiau.taskboardbackend.dal;

import be.restiau.taskboardbackend.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository <Task, Long> {
    List<Task> findByBoardColumnIdOrderByPosition(Long boardColumnId);
}
