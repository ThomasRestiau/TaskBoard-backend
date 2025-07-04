package be.restiau.taskboardbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"board", "tasks"})
@Entity
@Table(name = "board_column")
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Setter
    @Column(nullable = false, length = 120)
    private String name;

    @Setter
    @Column(nullable = false)
    private Integer position;

    /* 1 column → n tasks (bidirectionnel) */
    @OneToMany(mappedBy = "boardColumn",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("position ASC")
    private final List<Task> tasks = new ArrayList<>();

    public BoardColumn(String name, Integer position) {
        this.name =  name;
        this.position = position;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setBoardColumn(this);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
        task.setBoardColumn(null);
    }
}
