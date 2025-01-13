package fr.parisnanterre.iqplay.model;

import fr.parisnanterre.iqplay.service.OperationService;

import fr.parisnanterre.iqplay.validator.DefaultIAnswerValidator;
import fr.parisnanterre.iqplaylib.core.AbstractGame;
import fr.parisnanterre.iqplaylib.api.IGameSession;


public class GameCalculMental extends AbstractGame {
    private final OperationService operationService;

    public GameCalculMental(String name, OperationService operationService) {
        super(name);
        this.operationService = operationService;
    }

    @Override
    protected IGameSession createGameSession() {
        throw new UnsupportedOperationException("Game session creation is managed by GameCalculMentalService.");
    }
}

