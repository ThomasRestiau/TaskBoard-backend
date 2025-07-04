package be.restiau.taskboardbackend.bll.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.BoardColumn;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.BoardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardMapper boardMapper;

    @InjectMocks
    private BoardServiceImpl boardService;

    private User owner;
    private Board board;

    @BeforeEach
    void setUp() {
        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 99L);
        owner.setEmail("john@mail.com");

        board = new Board();
        board.setName("Mon board");
        board.setOwner(owner);
        // id & createdAt simulés après save
        board.addColumn(new BoardColumn()); // 3 colonnes s’ajouteront dans le service
    }

    @Test
    void create_should_persist_board_and_return_dto() {
        // ------ Arrange ------
        when(boardMapper.toEntity("Mon board", owner)).thenReturn(board);

        Board saved = board;                       // même instance mais id + dates simulées
        ReflectionTestUtils.setField(saved, "id", 1L);
        Instant now = Instant.now();
        ReflectionTestUtils.setField(saved, "createdAt", now);

        when(boardRepository.save(board))
                .thenReturn(saved);
        when(boardMapper.toDTO(saved))
                .thenReturn(new BoardResponse(1L, "Mon board", now));

        // ------ Act ------
        BoardResponse dto = boardService.create(owner, "Mon board");

        // ------ Assert ------
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Mon board");
        assertThat(dto.createdAt()).isEqualTo(now);

        verify(boardRepository).save(board);
    }

    @Test
    void create_should_throw_if_name_already_used() {
        when(boardRepository.existsByOwnerIdAndName(owner.getId(), "Mon board"))
                .thenReturn(true);

        assertThatThrownBy(() -> boardService.create(owner, "Mon board"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Board name already used");

        verify(boardRepository, never()).save(any());
    }
}
