package fr.parisnanterre.iqplay.api;

import fr.parisnanterre.iqplay.model.GameSession;

public interface ISessionState {
    void handle(GameSession session);
}

