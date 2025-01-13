package fr.parisnanterre.iqplay.model;

import fr.parisnanterre.iqplay.api.IAnswerValidator;
import fr.parisnanterre.iqplay.service.OperationService;

import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplaylib.core.AbstractGameSession;
import fr.parisnanterre.iqplaylib.core.QuestionStorageSession;

public class GameSession extends AbstractGameSession {
    private final OperationService operationService;
    private final IAnswerValidator validator;
    private final IPlayer player; // Nouveau champ pour stocker le joueur

    public GameSession(IGame game, OperationService operationService, IAnswerValidator validator, IPlayer player) {
        super(game);
        this.operationService = operationService;
        this.validator = validator;
        this.player = player;
        this.state = StateGameSessionEnum.CREATED;
    }

    @Override
    protected IQuestionStorageSession createQuestionStorageSession() {
        return new QuestionStorageSession(); // Retourne une instance valide
    }

    @Override
    protected IQuestionGenerator createQuestionGenerator() {
        return null; // Non utilisé
    }

    @Override
    public IQuestion nextQuestion() {
        int difficulty = level().level();
        IQuestion operation = operationService.createOperation("BasicOperationStrategy", difficulty);
        questionStorage.addQuestion(operation);
        return operation;
    }

    @Override
    public boolean submitAnswer(IPlayerAnswer answer) {
        IQuestion lastQuestion = questionStorage.lastQuestion();
        boolean isCorrect = validator.validate(lastQuestion, answer);

        if (isCorrect) {
            score.incrementScore(1);
            level.levelUp();
            onCorrectAnswer();
        } else {
            onIncorrectAnswer();
        }
        return isCorrect;
    }

    @Override
    public IQuestionStorageSession questionStorage() {
        return null;
    }

    public String name() {
        return game.name();
    }

    public void setState(StateGameSessionEnum newState) {
        this.state = newState;
    }

    public IPlayer getPlayer() { // Getter pour le joueur
        return player;
    }
}
