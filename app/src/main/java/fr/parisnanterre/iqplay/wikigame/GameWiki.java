package fr.parisnanterre.iqplay.wikigame;

import fr.parisnanterre.iqplay.wikigame.service.WikiOperationService;
import fr.parisnanterre.iqplaylib.api.IGameSession;
import fr.parisnanterre.iqplaylib.core.AbstractGame;

public class GameWiki extends AbstractGame {

    private final WikiOperationService wikiOperationService;

    public GameWiki(String name, WikiOperationService wikiOperationService) {
        super(name);
        this.wikiOperationService = wikiOperationService;
    }

    @Override
    protected IGameSession createGameSession() {
        return new GameSession(this, this.wikiOperationService);
    }
}
