package fr.parisnanterre.iqplay.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

import fr.parisnanterre.iqplay.mapper.GameSessionMapper;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.dto.GameStopResponseDto;
import fr.parisnanterre.iqplay.dto.GamePauseResponseDto;
import fr.parisnanterre.iqplay.dto.GameResumeResponseDto;
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
     * @param player The player for the session.
     * @param game The game for the session.
     * @return The ID of the session that has been persisted.
     */
    public Long startSession(IPlayer player, IGame game) {
        // Create the session
        IGameSession session = createSession(player, game);

        // Initialize session with default level and score
        session.start(new Level(1), new Score(0));
        activeSessions.put(getSessionId(session), (GameSession) session);

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

    public GameStopResponseDto endSession(Long sessionId) {
    // Trouve la session active ou chargée depuis la base
    GameSession session = (GameSession) findSession(sessionId);

    // Vérifie si la session existe
    if (session == null) {
        throw new IllegalArgumentException("Session non trouvée.");
    }

    // Vérifie si la session est déjà terminée
    if (session.state() == StateGameSessionEnum.ENDED) {
        throw new IllegalStateException("La session est déjà terminée.");
    }

    // Termine la session
    session.end();

    // Synchronise l'état de la session en base de données
    synchronizeSession(sessionId);

    // Supprime la session terminée de la map des sessions actives
    activeSessions.remove(sessionId);
    // Retourne une réponse avec les informations de fin de session
    return new GameStopResponseDto(
            GameMessageEnum.GAME_STOPPED.message(),
            session.score().score(),
            session.state().toString()
    );
}



    public List<GameSessionPersistante> getSessionsByPlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        return gameSessionRepository.findByPlayer(player);
    }

    public GamePauseResponseDto pauseSession(Long sessionId) {
        // Trouve la session active ou chargée depuis la base
        GameSession session = (GameSession) findSession(sessionId);

        // Vérifie si la session existe
        if (session == null) {
            throw new IllegalArgumentException("Session non trouvée.");
        }

        // Vérifie si la session est déjà terminée
        if (session.state() == StateGameSessionEnum.ENDED) {
            throw new IllegalStateException("La session est déjà terminée.");
        }

        // Vérifie si la session est en cours (IN_PROGRESS)
        if (session.state() != StateGameSessionEnum.IN_PROGRESS) {
            throw new IllegalStateException("La session n'est pas en cours.");
        }

        // Met à jour l'état de la session à PAUSED
        session.pause();

        // Synchronise l'état de la session en base de données
        synchronizeSession(sessionId);

        // Supprime la session mise en pause de la map des sessions actives
        activeSessions.remove(sessionId);

        // Retourne une réponse avec les informations de mise en pause
        return new GamePauseResponseDto(
                "Game paused successfully",
                session.score().score(),
                session.state().toString()
        );
    }

    public GameResumeResponseDto resumeSession(Long sessionId) {
        // Trouve la session persistante dans la base de données
        GameSessionPersistante persistante = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session non trouvée."));
        // Vérifie si la session est en pause
        if (!persistante.getState().equals(StateGameSessionEnum.PAUSED.name())) {
            throw new IllegalStateException("La session n'est pas en pause.");
        }

        // Transforme la session persistante en une session métier
        GameSession session = GameSessionMapper.toDomain(
                new GameCalculMental("Calcul Mental", operationService),
                persistante,
                operationService
        );

        // Ajoute la session à la map des sessions actives
        activeSessions.put(sessionId, session);

        // Synchronise l'état de la session en base de données
        synchronizeSession(sessionId);

        // Retourne une réponse avec les informations de reprise
        return new GameResumeResponseDto(
                "Game resumed successfully",
                session.score().score(),
                session.state().toString()
        );
    }
}
