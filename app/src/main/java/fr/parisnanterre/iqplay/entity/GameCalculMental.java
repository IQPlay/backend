package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplay.service.OperationService;

import fr.parisnanterre.iqplaylib.core.AbstractGame;
import fr.parisnanterre.iqplaylib.api.IGameSession;


public class GameCalculMental extends AbstractGame {
    private final OperationService operationService;

    /**
     * Represents a mental calculation game that extends the AbstractGame class.
     * Utilizes an OperationService for generating mathematical operations.
     *
     * <p>Dependencies are injected via the constructor.</p>
     *
     * @param name the name of the game
     * @param operationService the service used to create mathematical operations
     *
     * @see AbstractGame
     * @see OperationService
     */
    public GameCalculMental(String name, OperationService operationService) {
        super(name);
        this.operationService = operationService;
    }

    @Override
    protected IGameSession createGameSession() {
        return new GameSession(this, operationService);
    }
}
