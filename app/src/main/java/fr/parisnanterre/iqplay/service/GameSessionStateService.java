package fr.parisnanterre.iqplay.service;

import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameSessionStateService implements fr.parisnanterre.iqplay.api.IGameSessionStateService {

    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionStateService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public void updateState(Long sessionId, StateGameSessionEnum newState) {
        gameSessionRepository.findById(sessionId).ifPresent(persistante -> {
            persistante.setState(newState.toString());
            gameSessionRepository.save(persistante);
        });
    }
}
