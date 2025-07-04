package be.restiau.taskboardbackend.api.board;

import be.restiau.taskboardbackend.api.board.dto.*;
import be.restiau.taskboardbackend.bll.board.BoardService;
import be.restiau.taskboardbackend.bll.column.BoardColumnService;
import be.restiau.taskboardbackend.infra.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<BoardResponse> create(
            @AuthenticationPrincipal AppUserDetails principal,
            @Valid @RequestBody CreateBoardRequest payload
    ) {
        BoardResponse dto = boardService.create(principal.getUser(), payload.name());
        URI location = URI.create("/api/boards/" + dto.id());
        return ResponseEntity.created(location).body(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> rename(
            @AuthenticationPrincipal AppUserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody RenameBoardRequest payload
    ) {
        BoardResponse renamed = boardService.rename(id, principal.getUser().getId(), payload.newName());
        return ResponseEntity.ok(renamed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal AppUserDetails principal,
            @PathVariable Long id
    ) {
        boardService.delete(id, principal.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}/columns")
    public ResponseEntity<ColumnResponse> addColumn(
            @AuthenticationPrincipal AppUserDetails principal,
            @PathVariable Long boardId,
            @Valid @RequestBody CreateColumnRequest payload
    ){
        Long ownerId = principal.getUser().getId();
        ColumnResponse dto = boardColumnService.addColumn(boardId, ownerId, payload.name());
        URI location = URI.create("/api/boards/%d/columns/%d".formatted(boardId, dto.id()));
        return ResponseEntity.created(location).body(dto);
    }
}
