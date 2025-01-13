package fr.parisnanterre.iqplay.service;

import java.util.*;

import fr.parisnanterre.iqplay.api.IOperationStrategy;
import fr.parisnanterre.iqplaylib.core.CorrectAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplaylib.core.Question;

/**
 * Service class for generating mathematical operations and questions.
 *
 * This class provides methods to create sequences of mathematical questions
 * with varying difficulty levels, generate random numbers, and obtain random
 * mathematical operators. It also includes functionality to evaluate mathematical
 * expressions and create player responses.
 *
 * Methods:
 * - createSequenceNOperation(int difficulty): Generates a list of questions
 *   with varying difficulty based on the provided difficulty level.
 * - createOperation(int difficulty): Creates a single mathematical question
 *   with the specified difficulty.
 * - obtainOperands(int nbOperands): Returns a list of random mathematical
 *   operators based on the number of operands required.
 * - generateRandomNumberWithDigits(int numDigits): Generates a random number
 *   with the specified number of digits.
 * - createResponse(int givenAnswer): Constructs a player response with the
 *   given answer.
 */
@Service
public class OperationService {

    private final Map<String, IOperationStrategy> strategies = new HashMap<>();

    // Initialisation différée des stratégies pour éviter le cycle
    @Autowired
    public void setStrategies(List<IOperationStrategy> strategyList) {
        strategyList.forEach(strategy -> strategies.put(strategy.getClass().getSimpleName(), strategy));
    }

    public IQuestion createOperation(String strategyName, int difficulty) {
        IOperationStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
        String expression = strategy.generateExpression(difficulty);
        int result = strategy.evaluate(expression);
        return new Question(expression, new CorrectAnswer(String.valueOf(result)));
    }
}


