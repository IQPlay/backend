file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/service/GameCalculMentalService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1970
uri: file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/service/GameCalculMentalService.java
text:
```scala
package fr.parisnanterre.iqplay.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

import fr.parisnanterre.iqplay.mapper.GameSessionMapper;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.dto.GameStopResponseDto;
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
    public IGameSession createS@@ession(IPlayer player, IGame game) {
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
            session.state().name()
    );
}


    public List<GameSessionPersistante> getSessionsByPlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        return gameSessionRepository.findByPlayer(player);
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator