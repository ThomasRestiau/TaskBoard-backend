package be.restiau.taskboardbackend.bll.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.dal.BoardRepository;
import be.restiau.taskboardbackend.domain.Board;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.infra.mapper.BoardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardMapper boardMapper;
    @InjectMocks
    BoardServiceImpl boardService;

    User owner;
    Board board;

    @BeforeEach
    void setUp() {
        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 1L);
        board = new Board();
        board.setName("My board");
        board.setOwner(owner);
    }

    /* ---------- CREATE ---------- */

    @Test
    void create_ok_returns_dto() {
        when(boardRepository.existsByOwnerIdAndNameIgnoreCase(1L, "My board")).thenReturn(false);
        when(boardMapper.toEntity("My board", owner)).thenReturn(board);
        when(boardRepository.save(board)).thenReturn(board);
        when(boardMapper.toDTO(board))
                .thenReturn(new BoardResponse(10L, "My board", Instant.now()));

        BoardResponse dto = boardService.create(owner, "My board");

        assertThat(dto.name()).isEqualTo("My board");
        verify(boardRepository).save(board);
    }

    @Test
    void create_duplicateName_throws() {
        when(boardRepository.existsByOwnerIdAndNameIgnoreCase(1L, "My board")).thenReturn(true);

        assertThatThrownBy(() -> boardService.create(owner, "My board"))
                .isInstanceOf(IllegalStateException.class);
        verify(boardRepository, never()).save(any());
    }

    /* ---------- RENAME ---------- */

    @Test
    void rename_ok() {
        when(boardRepository.findByIdAndOwnerId(10L, 1L)).thenReturn(Optional.of(board));
        when(boardRepository.existsByOwnerIdAndNameIgnoreCase(1L, "New")).thenReturn(false);
        when(boardMapper.toDTO(board))
                .thenReturn(new BoardResponse(10L, "New", Instant.now()));

        BoardResponse dto = boardService.rename(10L, 1L, "New");
        assertThat(dto.name()).isEqualTo("New");
    }

    @Test
    void rename_duplicateName_throws() {
        when(boardRepository.findByIdAndOwnerId(10L, 1L)).thenReturn(Optional.of(board));
        when(boardRepository.existsByOwnerIdAndNameIgnoreCase(1L, "New")).thenReturn(true);

        assertThatThrownBy(() -> boardService.rename(10L, 1L, "New"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void rename_notOwner_throws403() {
        when(boardRepository.findByIdAndOwnerId(10L, 99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.rename(10L, 99L, "X"))
                .isInstanceOf(AccessDeniedException.class);
    }

    /* ---------- DELETE ---------- */

    @Test
    void delete_ok() {
        when(boardRepository.existsByIdAndOwnerId(10L, 1L)).thenReturn(true);

        boardService.delete(10L, 1L);

        verify(boardRepository).deleteById(10L);
    }

    @Test
    void delete_notOwner_throws403() {
        when(boardRepository.existsByIdAndOwnerId(10L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> boardService.delete(10L, 1L))
                .isInstanceOf(AccessDeniedException.class);
    }
}

