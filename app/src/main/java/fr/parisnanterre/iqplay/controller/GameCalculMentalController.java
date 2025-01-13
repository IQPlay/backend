package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing mental calculation game sessions.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/game")
public class GameCalculMentalController {

    private final GameCalculMentalService gameSessionService;
    private final PlayerService playerService;
    private final OperationService operationService;

    @Autowired
    public GameCalculMentalController(GameCalculMentalService gameSessionService,
                                      PlayerService playerService,
                                      OperationService operationService) {
        this.gameSessionService = gameSessionService;
        this.playerService = playerService;
        this.operationService = operationService;
    }

    @PostMapping("/start")
    public ResponseEntity<StartGameResponseDto> startGame() {
        try {
            IPlayer player = playerService.getCurrentPlayer();
            Long sessionId = gameSessionService.createSession(player, new GameCalculMental("Calcul Mental", operationService));

            return ResponseEntity.ok(new StartGameResponseDto(GameMessageEnum.SESSION_STARTED.message(), sessionId));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StartGameResponseDto(ex.getMessage(), null));
        }
    }

    @GetMapping("/operation/{sessionId}")
    public ResponseEntity<NextOperationResponseDto> getNextOperation(@PathVariable Long sessionId) {
        IQuestion nextQuestion = gameSessionService.getNextOperation(sessionId);
        return ResponseEntity.ok(new NextOperationResponseDto(null, nextQuestion.question(), "READY"));
    }

    @PostMapping("/answer/{sessionId}")
    public ResponseEntity<PlayerAnswerResponseDto> submitAnswer(
            @PathVariable Long sessionId, @RequestBody PlayerAnswerRequestDto playerAnswer) {
        try {
            boolean isCorrect = gameSessionService.submitAnswer(sessionId, playerAnswer.getAnswer());
            return ResponseEntity.ok(new PlayerAnswerResponseDto(
                    isCorrect ? "Correct!" : "Incorrect", isCorrect
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlayerAnswerResponseDto(
                    e.getMessage(), false
            ));
        }
    }

    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<GameStopResponseDto> stopGame(@PathVariable Long sessionId) {
        GameStopResponseDto response = gameSessionService.endSession(sessionId);
        return ResponseEntity.ok(response);
    }
}

