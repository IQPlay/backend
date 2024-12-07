package fr.parisnanterre.iqplay.services;

import fr.parisnanterre.iqplay.models.GameSession;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import fr.parisnanterre.iqplaylib.api.*;

@Service
public class GameCalculMentalService implements IGameSessionService {
    private final Map<Long, IGameSession> sessions = new HashMap<>();
    private long nextId = 1;

    private final OperationService operationService;

    /**
     * Service class for managing mental calculation game sessions.
     * Implements the IGameSessionService interface to provide functionality
     * for creating, retrieving, and managing game sessions.
     *
     * <p>Uses the OperationService to generate mathematical operations for
     * game sessions. Maintains a map of sessions identified by unique IDs.</p>
     *
     * @param operationService The service used to generate operations for questions.
     */
    public GameCalculMentalService(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     * Creates a new game session for the specified player and game.
     * Initializes the session using the provided game and the operation service,
     * and stores it in the sessions map with a unique ID.
     *
     * @param player The player for whom the session is created.
     * @param game The game instance associated with the session.
     * @return The newly created game session.
     */
    @Override
    public IGameSession createSession(IPlayer player, IGame game) {
        IGameSession session = new GameSession(game, operationService);
        sessions.put(nextId++, session);
        return session;
    }

    /**
     * Retrieves a game session by its unique session ID.
     *
     * @param sessionId The unique identifier of the session to retrieve.
     * @return The game session associated with the given session ID, or null if no session is found.
     */
    @Override
    public IGameSession findSession(Long sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Retrieves the unique session ID associated with the given game session.
     * Iterates through the stored sessions to find a match with the provided session object.
     *
     * @param session The game session for which the ID is to be retrieved.
     * @return The unique ID of the session, or null if no matching session is found.
     */
    @Override
    public Long getSessionId(IGameSession session) {
        // Parcourt les sessions pour trouver la correspondance avec l'objet session
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null); // Retourne null si aucune correspondance
    }

}

