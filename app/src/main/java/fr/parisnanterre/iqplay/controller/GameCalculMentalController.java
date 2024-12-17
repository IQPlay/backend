package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.Level;
import fr.parisnanterre.iqplay.model.Response;
import fr.parisnanterre.iqplay.model.Score;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplaylib.api.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public GameCalculMentalController(GameCalculMentalService gameSessionService, OperationService operationService) {
        this.gameSessionService = gameSessionService;
        this.operationService = operationService;
    }

    /**
     * Starts a new game session with the specified difficulty.
     *
     * @param request the difficulty level for the game session.
     * @return ResponseEntity containing the session ID.
     */
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody StartGameRequestDto request) {
        try {
            IPlayer player = new Player(); // Remplacez ceci par la récupération correcte du joueur
            IGame game = new GameCalculMental("Calcul Mental", operationService);
            IGameSession session = gameSessionService.createSession(player, game);
            Long sessionId = gameSessionService.getSessionId(session);
            return ResponseEntity.ok(new StartGameResponseDto(
                    GameMessageEnum.SESSION_STARTED.message(),
                    sessionId)
                    );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
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
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponseDto> validationResponse = validateSession(session);
        if (validationResponse != null) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody());
        }

        session.end();
        return ResponseEntity.ok(new GameStopResponseDto(
                GameMessageEnum.GAME_STOPPED.message(),
                session.score().score(),
                "ENDED"
        ));
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
