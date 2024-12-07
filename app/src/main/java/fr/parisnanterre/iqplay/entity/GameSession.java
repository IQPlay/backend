package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplay.service.OperationService;

import fr.parisnanterre.iqplaylib.core.AbstractGameSession;
import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplaylib.core.QuestionStorageSession;

import java.util.ArrayList;
import java.util.List;

public class GameSession extends AbstractGameSession {
    private final OperationService operationService;
    private final List<IQuestion> currentOperations; // Liste des opérations courantes

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
        this.currentOperations = new ArrayList<>();
    }

    @Override
    protected ILevel createLevel() {
        return new Level(); // Retourne une instance de votre classe Level
    }

    @Override
    protected IScore createScore() {
        return new Score(); // Retourne une instance de votre classe Score
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
        currentOperations.add(operation);
        questionStorage.addQuestion(operation);
        return operation;
    }


    @Override
    public IQuestionStorageSession questionStorage() {
        return this.questionStorage;
    }

    @Override
    public void submitAnswer(IPlayerAnswer answer) {
        questionStorage.addPlayerAnswer(answer);
        IQuestion lastQuestion = questionStorage.lastQuestion();
        ICorrectAnswer correctAnswer = lastQuestion.correctAnswer();

        // Vérifie si la réponse est correcte
        if (correctAnswer.answer().equals(answer.answer())) {
            score.incrementScore(1);
            level.levelUp();
            onCorrectAnswer();
        } else {
            onIncorrectAnswer();
        }
    }

}
