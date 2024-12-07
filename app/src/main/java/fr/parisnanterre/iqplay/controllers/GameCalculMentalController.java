package fr.parisnanterre.iqplay.controllers;

import fr.parisnanterre.iqplay.models.GameCalculMental;
import fr.parisnanterre.iqplay.models.Player;
import fr.parisnanterre.iqplay.models.Response;
import fr.parisnanterre.iqplay.services.GameCalculMentalService;
import fr.parisnanterre.iqplay.services.OperationService;

import fr.parisnanterre.iqplaylib.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing mental calculation game sessions.
 * Provides endpoints to start, stop, and interact with game sessions.
 *
 * <p>Endpoints:</p>
 * <ul>
 *   <li>/start: Initiates a new game session with a specified difficulty.</li>
 *   <li>/operation/{sessionId}: Retrieves the next operation for a given session.</li>
 *   <li>/answer/{sessionId}: Submits an answer for the current operation in a session.</li>
 *   <li>/stop/{sessionId}: Stops an ongoing game session.</li>
 * </ul>
 *
 * <p>Uses GameCalculMentalService for session management and OperationService
 * for generating operations.</p>
 *
 * @see GameCalculMentalService
 * @see OperationService
 */
@RestController
@RequestMapping("/api/game")
public class GameCalculMentalController {
    private final GameCalculMentalService gameSessionService;
    private final OperationService operationService;

    private static final String message = "message";
    private static final String score = "score";
    private static final String gameStatus = "status";

    /**
     * Constructs a GameCalculMentalController with the specified services.
     *
     * @param gameSessionService the service for managing game sessions
     * @param operationService the service for generating operations
     */
    @Autowired
    public GameCalculMentalController(GameCalculMentalService gameSessionService, OperationService operationService) {
        this.gameSessionService = gameSessionService;
        this.operationService = operationService;
    }

    /**
     * Starts a new mental calculation game session with the specified difficulty.
     * Initializes a default player and game, creates a session, and starts it.
     * Returns a response entity containing a message and the session ID.
     *
     * @param difficulty the difficulty level for the game session
     * @return ResponseEntity containing a message and the session ID
     */
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam int difficulty) {
        IPlayer player = new Player("Default User");
        IGame game = new GameCalculMental("Calcul Mental", operationService);
        IGameSession session = gameSessionService.createSession(player, game);
        session.start();

        Long sessionId = gameSessionService.getSessionId(session);

        return ResponseEntity.ok(Map.of(
                GameCalculMentalController.message, GameMessageEnum.SESSION_STARTED.message(),
                "sessionId", sessionId
        ));
    }

    /**
     * Retrieves the next operation for a given game session.
     * Checks if the session exists and is active, and if there are no pending operations.
     * Returns the next operation or an appropriate error message if conditions are not met.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the next operation or an error message.
     */
    @GetMapping("/operation/{sessionId}")
    public ResponseEntity<?> getNextOperation(@PathVariable Long sessionId) {
        IGameSession session = gameSessionService.findSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.SESSION_NOT_FOUND.message(),
                    GameCalculMentalController.gameStatus, "UNKNOWN"
            ));
        }

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(400).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.GAME_ENDED_NO_OPERATION.message(),
                    GameCalculMentalController.score, session.score().score(),
                    GameCalculMentalController.gameStatus, "ENDED"
            ));
        }

        // Vérifie si une opération précédente est en attente de réponse
        IQuestion lastQuestion = session.questionStorage().lastQuestion();
        if (lastQuestion != null && session.questionStorage().lastPlayerAnswer() == null) {
            return ResponseEntity.status(400).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.OPERATION_PENDING.message(),
                    "question", lastQuestion.question(),
                    GameCalculMentalController.gameStatus, "BLOCKED"
            ));
        }

        // Génère une nouvelle opération si aucune n'est en attente
        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(Map.of(
                "question", nextQuestion.question()
        ));
    }

    /**
     * Submits a player's answer for the current operation in a game session.
     * Validates the session's existence and state before processing the answer.
     * Returns a response entity with the result of the submission, including
     * the next question if the game continues, or the final score if the game ends.
     *
     * @param sessionId The unique identifier of the game session.
     * @param userAnswer The answer provided by the player.
     * @return ResponseEntity containing the result of the answer submission,
     *         including messages, score, and game status.
     */
    @PostMapping("/answer/{sessionId}")
    public ResponseEntity<?> submitAnswer(@PathVariable Long sessionId, @RequestParam int userAnswer) {
        IGameSession session = gameSessionService.findSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.SESSION_NOT_FOUND.message(),
                    GameCalculMentalController.gameStatus, "UNKNOWN"
            ));
        }

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(400).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.GAME_ENDED_NO_RESPONSE.message(),
                    GameCalculMentalController.score, session.score().score(),
                    GameCalculMentalController.gameStatus, "ENDED"
        ));
        }

        IPlayerAnswer answer = new Response(userAnswer);
        session.submitAnswer(answer);

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.ok(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.GAME_ENDED_NO_RESPONSE.message(),
                    GameCalculMentalController.score, session.score().score(),
                    GameCalculMentalController.gameStatus, "ENDED"
            ));
        }

        IQuestion nextQuestion = session.nextQuestion();
        return ResponseEntity.ok(Map.of(
                GameCalculMentalController.message, GameMessageEnum.ANSWER_CORRECT.message(),
                GameCalculMentalController.score, session.score().score(),
                "nextQuestion", nextQuestion.question()
        ));
    }

    /**
     * Stops an ongoing mental calculation game session.
     * Validates the session's existence and state before stopping it.
     * Returns a response entity with the result of the stop action,
     * including messages, score, and game status.
     *
     * @param sessionId The unique identifier of the game session.
     * @return ResponseEntity containing the result of the stop action,
     *         including messages, score, and game status.
     */
    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<?> stopGame(@PathVariable Long sessionId) {
        IGameSession session = gameSessionService.findSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.SESSION_NOT_FOUND.message(),
                    GameCalculMentalController.gameStatus, "UNKNOWN"
            ));
        }

        if (session.state() == StateGameSessionEnum.ENDED) {
            return ResponseEntity.status(400).body(Map.of(
                    GameCalculMentalController.message, GameMessageEnum.GAME_ALREADY_ENDED.message(),
                    GameCalculMentalController.score, session.score().score(),
                    GameCalculMentalController.gameStatus, "ENDED"
        ));
        }

        session.end();
        return ResponseEntity.ok(Map.of(
                GameCalculMentalController.message, GameMessageEnum.GAME_STOPPED.message(),
                GameCalculMentalController.score, session.score().score(),
                GameCalculMentalController.gameStatus, "ENDED"
        ));
    }

}
