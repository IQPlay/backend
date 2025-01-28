package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.Response;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.*;

import java.util.Map;

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
    private final OperationService operationService;
    private final PlayerService playerService;

    @Autowired
    public GameCalculMentalController(GameCalculMentalService gameSessionService, OperationService operationService, PlayerService playerService) {
        this.gameSessionService = gameSessionService;
        this.operationService = operationService;
        this.playerService=playerService;
        
    }

    /**
     * Starts a new game session with the specified difficulty.
     *
     * @param request the difficulty level for the game session.
     * @return ResponseEntity containing the session ID.
     */
    @PostMapping("/start")
    public ResponseEntity<StartGameResponseDto> startGame(@RequestBody StartGameRequestDto request) {
        try {
            IPlayer player = playerService.getCurrentPlayer();
            IGame game = new GameCalculMental("Calcul Mental", operationService);

            Long sessionId = gameSessionService.startSession(player, game);
            return ResponseEntity.ok(new StartGameResponseDto(
                    GameMessageEnum.SESSION_STARTED.message(),
                    sessionId
            ));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StartGameResponseDto(
                    ex.getMessage(),
                    null
            ));
        }
    }

    /**
     * Retrieves the next operation for the specified session.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the next operation or an error message.
     */
    @GetMapping("/operation/{sessionId}")
    public ResponseEntity<NextOperationResponseDto> getNextOperation(@PathVariable Long sessionId) {
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponseDto> validationResponse = validateSession(session);
        if (validationResponse != null) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(null);
        }

        // Vérifie si la session est terminée
        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NextOperationResponseDto(
                    GameMessageEnum.GAME_ENDED_NO_OPERATION.message(),
                    null,
                    "ENDED"
            ));
        }

        // Vérifie si la session est en cours
        if (session.state() != StateGameSessionEnum.IN_PROGRESS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NextOperationResponseDto(
                    GameMessageEnum.GAME_ENDED_NO_OPERATION.message(),
                    null,
                    "UNKNOWN"
            ));
        }

        IQuestion lastQuestion = session.questionStorage().lastQuestion();
        if (lastQuestion != null && session.questionStorage().lastPlayerAnswer() == null) {
            return ResponseEntity.ok(new NextOperationResponseDto(
                    GameMessageEnum.OPERATION_PENDING.message(),
                    lastQuestion.question(),
                    "BLOCKED"
            ));
        }

        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(new NextOperationResponseDto(
                null,
                nextQuestion.question(),
                "READY"
        ));
    }

    /**
     * Submits a player's answer for the current operation in a game session.
     *
     * @param sessionId The unique identifier of the game session.
     * @param request The answer provided by the player.
     * @return ResponseEntity containing the result of the submission.
     */
    @PostMapping("/answer/{sessionId}")
    public ResponseEntity<SubmitAnswerResponseDto> submitAnswer(
            @PathVariable Long sessionId,
            @RequestBody SubmitAnswerRequestDto request
    ) {
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponseDto> validationResponse = validateSession(session);
        if (validationResponse != null) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(null);
        }

        IPlayerAnswer answer = new Response(request.getUserAnswer());
        session.submitAnswer(answer);

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.ok(new SubmitAnswerResponseDto(
                    GameMessageEnum.GAME_ENDED_NO_RESPONSE.message(),
                    session.score().score(),
                    "ENDED",
                    null
            ));
        }

        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(new SubmitAnswerResponseDto(
                GameMessageEnum.ANSWER_CORRECT.message(),
                session.score().score(),
                "IN_PROGRESS",
                nextQuestion.question()
        ));
    }

    /**
     * Stops an ongoing game session.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the result of the stop action.
     */
    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<GameStopResponseDto> stopGame(@PathVariable Long sessionId) {
        // Trouve la session
        IGameSession session = gameSessionService.findSession(sessionId);

        // Valide la session via validateSession
        ResponseEntity<GameStopResponseDto> validationResponse = validateSession(session);
        if (validationResponse != null) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody());
        }

        // Termine la session en appelant le service
        GameStopResponseDto response = gameSessionService.endSession(sessionId);

        // Retourne une réponse avec succès
        return ResponseEntity.ok(response);
}

    /**
     * Pauses an ongoing game session.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the result of the pause action.
     */
    @PostMapping("/pause/{sessionId}")
    public ResponseEntity<GamePauseResponseDto> pauseGame(@PathVariable Long sessionId) {
        try {
            GamePauseResponseDto response = gameSessionService.pauseSession(sessionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GamePauseResponseDto(
                    ex.getMessage(),
                    0,
                    "UNKNOWN"
            ));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new GamePauseResponseDto(
                    ex.getMessage(),
                    0,
                    "ENDED"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GamePauseResponseDto(
                    "Failed to pause the session",
                    0,
                    "ERROR"
            ));
        }
    }

    /**
     * Resumes a paused game session.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the result of the resume action.
     */
    @PostMapping("/resume/{sessionId}")
    public ResponseEntity<GameResumeResponseDto> resumeGame(@PathVariable Long sessionId) {
        try {
            GameResumeResponseDto response = gameSessionService.resumeSession(sessionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameResumeResponseDto(
                    ex.getMessage(),
                    0,
                    "UNKNOWN"
            ));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new GameResumeResponseDto(
                    ex.getMessage(),
                    0,
                    "ENDED"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GameResumeResponseDto(
                    "Failed to resume the session",
                    0,
                    "ERROR"
            ));
        }
    }

    /**
     * Validates the session existence and state.
     *
     * @param session The game session to validate.
     * @return ResponseEntity if invalid, null if valid.
     */
    private ResponseEntity<GameStopResponseDto> validateSession(IGameSession session) {
        if (session == null) {
            return ResponseEntity.status(404).body(new GameStopResponseDto(
                    GameMessageEnum.SESSION_NOT_FOUND.message(),
                    0,
                    "UNKNOWN"
            ));
        }

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(400).body(new GameStopResponseDto(
                    GameMessageEnum.GAME_ALREADY_ENDED.message(),
                    session.score().score(),
                    "ENDED"
            ));
        }

        return null;
    }
}
