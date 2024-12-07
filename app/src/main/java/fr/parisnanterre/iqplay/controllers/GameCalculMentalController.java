package fr.parisnanterre.iqplay.controllers;

import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.models.GameCalculMental;
import fr.parisnanterre.iqplay.models.Player;
import fr.parisnanterre.iqplay.models.Response;
import fr.parisnanterre.iqplay.services.GameCalculMentalService;
import fr.parisnanterre.iqplay.services.OperationService;
import fr.parisnanterre.iqplaylib.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing mental calculation game sessions.
 */
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
    public ResponseEntity<StartGameResponse> startGame(@RequestBody StartGameRequest request) {
        IPlayer player = new Player("Default User");
        IGame game = new GameCalculMental("Calcul Mental", operationService);
        IGameSession session = gameSessionService.createSession(player, game);
        session.start();

        Long sessionId = gameSessionService.getSessionId(session);
        return ResponseEntity.ok(new StartGameResponse(
                GameMessageEnum.SESSION_STARTED.message(),
                sessionId
        ));
    }

    /**
     * Retrieves the next operation for the specified session.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the next operation or an error message.
     */
    @GetMapping("/operation/{sessionId}")
    public ResponseEntity<?> getNextOperation(@PathVariable Long sessionId) {
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponse> validationResponse = validateSession(session);
        if (validationResponse != null) return validationResponse;

        IQuestion lastQuestion = session.questionStorage().lastQuestion();
        if (lastQuestion != null && session.questionStorage().lastPlayerAnswer() == null) {
            return ResponseEntity.status(400).body(new NextOperationResponse(
                    GameMessageEnum.OPERATION_PENDING.message(),
                    lastQuestion.question(),
                    "BLOCKED"
            ));
        }

        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(new NextOperationResponse(
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
    public ResponseEntity<?> submitAnswer(@PathVariable Long sessionId, @RequestBody SubmitAnswerRequest request) {
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponse> validationResponse = validateSession(session);
        if (validationResponse != null) return validationResponse;

        IPlayerAnswer answer = new Response(request.getUserAnswer());
        session.submitAnswer(answer);

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.ok(new SubmitAnswerResponse(
                    GameMessageEnum.GAME_ENDED_NO_RESPONSE.message(),
                    session.score().score(),
                    "ENDED",
                    null
            ));
        }

        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(new SubmitAnswerResponse(
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
    public ResponseEntity<GameStopResponse> stopGame(@PathVariable Long sessionId) {
        IGameSession session = gameSessionService.findSession(sessionId);
        ResponseEntity<GameStopResponse> validationResponse = validateSession(session);
        if (validationResponse != null) return validationResponse;

        session.end();
        return ResponseEntity.ok(new GameStopResponse(
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
    private ResponseEntity<GameStopResponse> validateSession(IGameSession session) {
        if (session == null) {
            return ResponseEntity.status(404).body(new GameStopResponse(
                    GameMessageEnum.SESSION_NOT_FOUND.message(),
                    0,
                    "UNKNOWN"
            ));
        }

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(400).body(new GameStopResponse(
                    GameMessageEnum.GAME_ALREADY_ENDED.message(),
                    session.score().score(),
                    "ENDED"
            ));
        }

        return null;
    }
}
