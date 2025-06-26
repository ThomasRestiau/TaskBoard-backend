package be.restiau.taskboardbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"owner", "columns"})
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @Setter
    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /* 1 board → n columns (bidirectionnel) */
    @OneToMany(mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("position ASC")
    private final List<BoardColumn> columns = new ArrayList<>();

    /* helpers pour garder la cohérence mémoire */
    public void addColumn(BoardColumn col) {
        columns.add(col);
        col.setBoard(this);
    }
    public void removeColumn(BoardColumn col) {
        columns.remove(col);
        col.setBoard(null);
    }
}
