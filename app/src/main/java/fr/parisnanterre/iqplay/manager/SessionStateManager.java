package fr.parisnanterre.iqplay.manager;

import fr.parisnanterre.iqplay.api.ISessionState;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.state.EndedState;
import fr.parisnanterre.iqplay.state.StartedState;
import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionStateManager {

    private final Map<StateGameSessionEnum, ISessionState> states = new HashMap<>();

    public SessionStateManager() {
        states.put(StateGameSessionEnum.STARTED, new StartedState());
        states.put(StateGameSessionEnum.ENDED, new EndedState());
        // Ajoutez d'autres états ici
    }

    public void transitionTo(GameSession session, StateGameSessionEnum newState) {
        if (session.state() == StateGameSessionEnum.ENDED) {
            throw new IllegalStateException("Cannot transition from ENDED state.");
        }
        states.get(newState).handle(session);
    }

}
