package fr.parisnanterre.iqplay.model;

import fr.parisnanterre.iqplay.service.OperationService;

import fr.parisnanterre.iqplaylib.core.AbstractGameSession;
import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplaylib.core.QuestionStorageSession;

import java.util.ArrayList;
import java.util.List;

public class GameSession extends AbstractGameSession {
    private final OperationService operationService;
    
    /**
     * Represents a game session that extends the AbstractGameSession class.
     * Manages the lifecycle of a game session, including creating levels, scores,
     * and handling questions and player answers.
     *
     * <p>Uses the OperationService to generate mathematical operations as questions
     * based on the current difficulty level. It maintains a list of current operations
     * and interacts with the question storage session to manage questions and answers.</p>
     *
     * <p>Overrides methods to create instances of Level, Score, and QuestionStorageSession,
     * and provides functionality to submit and evaluate player answers.</p>
     *
     * @param game The game instance associated with this session.
     * @param operationService The service used to generate operations for questions.
     */
    public GameSession(IGame game, OperationService operationService) {
        super(game);
        this.operationService = operationService;
    }

    @Override
    public void start(ILevel level, IScore score) {
        this.state = StateGameSessionEnum.IN_PROGRESS;
        this.level = new Level(level.level());
        this.score = new Score(score.score());
     }
    
     
    @Override
    protected IQuestionStorageSession createQuestionStorageSession() {
        return new QuestionStorageSession(); // Utilise QuestionStorageSession
    }

    @Override
    protected IQuestionGenerator createQuestionGenerator() {
        return null; // Non utilisé car la génération est gérée par OperationService
    }

    @Override
    public IQuestion nextQuestion() {
        int difficulty = level().level(); // Utilise le niveau actuel comme difficulté
        IQuestion operation = operationService.createOperation(difficulty);
        questionStorage.addQuestion(operation);
        return operation;
    }


    @Override
    public IQuestionStorageSession questionStorage() {
        return this.questionStorage;
    }
    
    public String name(){
        return game.name();
    }
    

}
