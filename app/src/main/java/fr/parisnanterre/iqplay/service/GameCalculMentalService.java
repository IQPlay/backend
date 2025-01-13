package fr.parisnanterre.iqplay.service;

import fr.parisnanterre.iqplay.dto.GameStopResponseDto;
import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.manager.ActiveSessionManager;
import fr.parisnanterre.iqplay.manager.SessionStateManager;
import fr.parisnanterre.iqplay.mapper.GameSessionMapper;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.Level;
import fr.parisnanterre.iqplay.model.Score;
import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplay.validator.DefaultIAnswerValidator;
import fr.parisnanterre.iqplaylib.core.PlayerAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplay.model.GameSession;

import java.util.List;

@Service
public class GameCalculMentalService {

    private static final Logger log = LoggerFactory.getLogger(GameCalculMentalService.class);
    private final SessionStateManager stateManager;
    private final ActiveSessionManager activeSessionManager;
    private final GameSessionRepository gameSessionRepository;
    private final OperationService operationService;

    @Autowired
    public GameCalculMentalService(SessionStateManager stateManager,
                                   ActiveSessionManager activeSessionManager,
                                   GameSessionRepository gameSessionRepository,
                                   OperationService operationService) { // Injecter OperationService
        this.stateManager = stateManager;
        this.activeSessionManager = activeSessionManager;
        this.gameSessionRepository = gameSessionRepository;
        this.operationService = operationService;
    }

    public Long createSession(IPlayer player, IGame game) {
        GameSession session = new GameSession(game, operationService, new DefaultIAnswerValidator(), player);
        session.start(new Level(1), new Score(0));

        Player playerEntity = (Player) player;
        GameSessionPersistante persistableSession = GameSessionMapper.toPersistable(session, playerEntity);
        persistableSession.setState(StateGameSessionEnum.CREATED.toString());

        GameSessionPersistante savedSession = gameSessionRepository.save(persistableSession);

        Long sessionId = savedSession.getId();
        activeSessionManager.addSession(sessionId, session);
        return sessionId;
    }

    public IQuestion getNextOperation(Long sessionId) {
        GameSession session = (GameSession) activeSessionManager.getSession(sessionId)
                .orElseGet(() -> gameSessionRepository.findById(sessionId)
                        .map(persistable -> {
                            GameSession restoredSession = GameSessionMapper.toDomain(
                                    new GameCalculMental("Calcul Mental", operationService),
                                    persistable,
                                    operationService
                            );
                            activeSessionManager.addSession(sessionId, restoredSession);
                            return restoredSession;
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Session not found"))
                );

        // Transition de l'état si nécessaire
        if (session.state() == StateGameSessionEnum.ENDED) {
            throw new IllegalStateException("Cannot generate new operations for an ended session.");
        }

        if (session.state() == StateGameSessionEnum.CREATED || session.state() == StateGameSessionEnum.PAUSED) {
            stateManager.transitionTo(session, StateGameSessionEnum.STARTED);
        }

        return session.nextQuestion();
    }

    public boolean submitAnswer(Long sessionId, String playerAnswer) {
        GameSession session = (GameSession) activeSessionManager.getSession(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.state() == StateGameSessionEnum.ENDED) {
            throw new IllegalStateException("Session has already ended.");
        }

        IPlayerAnswer answer = new PlayerAnswer(playerAnswer);
        boolean isCorrect = session.submitAnswer(answer);

        if (isCorrect) {
            stateManager.transitionTo(session, StateGameSessionEnum.IN_PROGRESS);
            gameSessionRepository.save(GameSessionMapper.toPersistable(session, (Player) session.getPlayer()));
        } else {
            stateManager.transitionTo(session, StateGameSessionEnum.ENDED);
            gameSessionRepository.save(GameSessionMapper.toPersistable(session, (Player) session.getPlayer()));
            activeSessionManager.removeSession(sessionId);
        }
        return isCorrect;
    }


    public GameStopResponseDto endSession(Long sessionId) {
        GameSession session = (GameSession) activeSessionManager.getSession(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        stateManager.transitionTo(session, StateGameSessionEnum.ENDED);
        activeSessionManager.removeSession(sessionId);

        return new GameStopResponseDto(GameMessageEnum.GAME_STOPPED.message(), session.score().score(), "ENDED");
    }

    public List<GameSessionPersistante> getSessionsByPlayer(IPlayer player) {
        return gameSessionRepository.findByPlayer(player);
    }
}
