package fr.parisnanterre.iqplay.wikigame;

import fr.parisnanterre.iqplay.wikigame.service.WikiOperationService;
import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplaylib.core.AbstractGameSession;
import fr.parisnanterre.iqplaylib.core.QuestionStorageSession;

public class GameSession extends AbstractGameSession {

    private final WikiOperationService wikiOperationService;

    public GameSession(IGame game, WikiOperationService wikiOperationService) {
        super(game);
        this.wikiOperationService = wikiOperationService;
    }

    @Override
    public void start(ILevel level, IScore score) {
        this.state = StateGameSessionEnum.IN_PROGRESS;
    }


    @Override
    protected IQuestionStorageSession createQuestionStorageSession() {
        return new QuestionStorageSession();
    }

    @Override
    protected IQuestionGenerator createQuestionGenerator() {
        return null;
    }

    @Override
    public IQuestion nextQuestion() {
        /*
        * @TODO à définir quand on va parcourir les questions de la fiche
         */
        return null;
    }

    @Override
    public IQuestionStorageSession questionStorage() {
        return this.questionStorage;
    }

    public String name(){
        return game.name();
    }
}
