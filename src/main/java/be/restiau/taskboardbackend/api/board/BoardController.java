package be.restiau.taskboardbackend.api.board;

import be.restiau.taskboardbackend.api.board.dto.BoardResponse;
import be.restiau.taskboardbackend.api.board.dto.CreateBoardRequest;
import be.restiau.taskboardbackend.bll.board.BoardService;
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

    @PostMapping
    public ResponseEntity<BoardResponse> create(
            @AuthenticationPrincipal AppUserDetails principal,
            @Valid @RequestBody CreateBoardRequest payload
    ){
        BoardResponse dto = boardService.create(principal.getUser(), payload.name());
        URI location = URI.create("api/boards" + dto.id());
        return ResponseEntity.created(location).body(dto);
    }
}
