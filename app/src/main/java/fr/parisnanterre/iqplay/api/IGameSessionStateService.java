package fr.parisnanterre.iqplay.api;

import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;

public interface IGameSessionStateService {
    void updateState(Long sessionId, StateGameSessionEnum newState);
}
