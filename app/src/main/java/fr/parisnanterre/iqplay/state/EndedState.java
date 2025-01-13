package fr.parisnanterre.iqplay.state;

import fr.parisnanterre.iqplay.api.ISessionState;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;

public class EndedState implements ISessionState {

    @Override
    public void handle(GameSession session) {
        session.setState(StateGameSessionEnum.ENDED);
        // Ajoutez des actions spécifiques si nécessaire
    }
}