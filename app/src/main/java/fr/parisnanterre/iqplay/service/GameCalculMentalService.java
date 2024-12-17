package fr.parisnanterre.iqplay.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import fr.parisnanterre.iqplay.mapper.GameSessionMapper;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplay.model.Level;
import fr.parisnanterre.iqplay.model.Score;
import fr.parisnanterre.iqplaylib.api.*;

@Service
public class GameCalculMentalService implements IGameSessionService {
    private final Map<Long, GameSession> activeSessions = new ConcurrentHashMap<>();
    private final OperationService operationService;
    private final GameSessionRepository gameSessionRepository;

    public GameCalculMentalService(OperationService operationService, GameSessionRepository gameSessionRepository) {
        this.operationService = operationService;
        this.gameSessionRepository = gameSessionRepository;
    }

    /**
     * Starts a new game session.
     * 
     * @return The ID of the session that has been persisted.
     */
    public Long startSession() {
        IPlayer player = new Player();
        IGame game = new GameCalculMental("Calcul Mental", operationService);

        // Create the session
        IGameSession session = createSession(player, game);

        // Initialize session with default level and score
        session.start(new Level(1), new Score(0));

        // Return the session ID
        return getSessionId(session);
    }

    /**
     * Creates a new session for a player and game, persists it, and adds it to memory.
     */
    @Override
    public IGameSession createSession(IPlayer player, IGame game) {
        GameSession session = new GameSession(game, operationService);

        // Ensure the session is initialized with default level and score
        session.start(new Level(1), new Score(0));

        // Map to a persistable entity and save to database
        GameSessionPersistante persistante = GameSessionMapper.toPersistable(session, (fr.parisnanterre.iqplay.entity.Player) player);
        GameSessionPersistante savedEntity = gameSessionRepository.save(persistante);

        // Store in memory
        activeSessions.put(savedEntity.getId(), session);

        return session;
    }

    @Override
    public IGameSession findSession(Long sessionId) {
        GameSession session = activeSessions.get(sessionId);
        if (session != null) {
            return session;
        }

        return gameSessionRepository.findById(sessionId)
                .map(persistante -> {
                    GameSession loadedSession = GameSessionMapper.toDomain(
                            new GameCalculMental("Calcul Mental", operationService), 
                            persistante, 
                            operationService
                    );
                    activeSessions.put(sessionId, loadedSession);
                    return loadedSession;
                })
                .orElse(null);
    }

    @Override
    public Long getSessionId(IGameSession session) {
        return activeSessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void synchronizeSession(Long sessionId) {
        GameSession session = activeSessions.get(sessionId);
        if (session != null) {
            gameSessionRepository.findById(sessionId).ifPresent(persistante -> {
                GameSessionMapper.updatePersistable(session, persistante);
                gameSessionRepository.save(persistante);
            });
        }
    }

    public void endSession(Long sessionId) {
        synchronizeSession(sessionId);
        activeSessions.remove(sessionId);
    }
}
