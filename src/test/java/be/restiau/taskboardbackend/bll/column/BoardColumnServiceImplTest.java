package be.restiau.taskboardbackend.bll.column;

import be.restiau.taskboardbackend.api.board.dto.ColumnResponse;
import be.restiau.taskboardbackend.dal.BoardColumnRepository;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.BoardColumnMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardColumnServiceImplTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardColumnRepository boardColumnRepository;
    @Mock
    BoardColumnMapper boardColumnMapper;

    @InjectMocks
    BoardColumnServiceImpl boardColumnService;

    User owner;
    Board board;

    @BeforeEach
    void setUp() {
        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 9L);

        board = new Board();
        ReflectionTestUtils.setField(board, "id", 10L);
        board.setName("Mon board");
        ReflectionTestUtils.setField(board, "owner", owner);
    }

    @Test
    void addColumn_should_add_new_column_and_return_dto() {
        when(boardRepository.findByIdAndOwnerId(10L, 9L))
                .thenReturn(Optional.of(board));
        when(boardColumnRepository.existsByBoardIdAndNameIgnoreCase(10L, "Urgent"))
                .thenReturn(false);
        when(boardColumnRepository.countByBoardId(10L))
                .thenReturn(3);

        when(boardColumnMapper.toDTO(any()))
                .thenReturn(new ColumnResponse(15L, "Urgent", 3));

        ColumnResponse dto = boardColumnService.addColumn(10L, 9L, "Urgent");

        assertThat(dto.id()).isEqualTo(15L);
        assertThat(dto.Name()).isEqualTo("Urgent");
        assertThat(dto.Position()).isEqualTo(3);

        verify(boardColumnRepository).existsByBoardIdAndNameIgnoreCase(10L, "Urgent");
        verify(boardColumnRepository).countByBoardId(10L);
        verifyNoMoreInteractions(boardColumnRepository);
    }

    @Test
    void addColumn_should_throw_if_name_already_exists() {
        when(boardRepository.findByIdAndOwnerId(10L, 9L))
                .thenReturn(Optional.of(board));
        when(boardColumnRepository.existsByBoardIdAndNameIgnoreCase(10L, "Urgent"))
                .thenReturn(true);

        assertThatThrownBy(() -> boardColumnService.addColumn(10L, 9L, "Urgent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Column name already exists");

        verify(boardColumnRepository, never()).countByBoardId(anyLong());
        verify(boardColumnMapper, never()).toDTO(any());
    }

    @Test
    void addColumn_should_throw_if_not_owner_or_board_not_found() {
        when(boardRepository.findByIdAndOwnerId(10L, 9L))
                .thenReturn(Optional.empty());      // pas trouvé / pas owner

        assertThatThrownBy(() -> boardColumnService.addColumn(10L, 9L, "Urgent"))
                .isInstanceOf(AccessDeniedException.class);

        verify(boardColumnRepository, never()).existsByBoardIdAndNameIgnoreCase(anyLong(), anyString());
        verify(boardColumnMapper, never()).toDTO(any());
    }
}
