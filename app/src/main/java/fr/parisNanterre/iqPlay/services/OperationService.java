package fr.parisnanterre.iqplay.services;

import fr.parisnanterre.iqplay.models.Response;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import fr.parisnanterre.iqplaylib.core.CorrectAnswer;
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
    private final Random random = new Random();
    private static final int DEFAULT_SEQUENCE_SIZE = 3;

    /**
     * Generates a sequence of mathematical questions with varying difficulty levels.
     *
     * This method creates a list of questions, each with a difficulty level
     * adjusted around the provided base difficulty. The sequence size is determined
     * by a default constant, and each question is generated using the createOperation
     * method.
     *
     * @param difficulty the base difficulty level for the sequence of questions
     * @return a list of IQuestion objects representing the generated questions
     */
    public List<IQuestion> createSequenceNOperation(int difficulty) {
        List<IQuestion> operations = new ArrayList<>();

        // Crée une série de questions avec différentes difficultés
        for (int i = 0; i < DEFAULT_SEQUENCE_SIZE; i++) {
            int currentDifficulty = difficulty + (i - DEFAULT_SEQUENCE_SIZE / 2);
            IQuestion question = createOperation(currentDifficulty);
            operations.add(question);
        }

        return operations;
    }

    /**
     * Creates a single mathematical question with the specified difficulty level.
     *
     * This method generates a mathematical expression by obtaining a list of random
     * operators and numbers based on the given difficulty. It constructs the expression
     * as a string, evaluates it to find the result, and returns a Question object
     * containing the expression and its correct answer.
     *
     * @param difficulty the difficulty level for generating the mathematical question
     * @return an IQuestion object representing the generated mathematical question
     */
    public IQuestion createOperation(int difficulty) {
        // Obtenir les opérateurs
        List<Character> operands = obtainOperands(difficulty % 3 + 2);

        // Générer les nombres
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= operands.size(); i++) {
            numbers.add(generateRandomNumberWithDigits(1 + difficulty / 10));
        }

        // Construire l'expression mathématique
        StringBuilder expression = new StringBuilder();
        expression.append(numbers.get(0));
        for (int i = 0; i < operands.size(); i++) {
            expression.append(" ").append(operands.get(i)).append(" ").append(numbers.get(i + 1));
        }

        // Évaluer l'expression
        int result = Evaluator.evaluate(expression.toString());

        // Retourner une question
        return new Question(expression.toString(), new CorrectAnswer(String.valueOf(result)));
    }

    /**
     * Returns a list of random mathematical operators.
     *
     * This method generates a list of random operators ('*', '/', '-', '+')
     * based on the specified number of operands. It ensures that no more than
     * two multiplication or division operators are included in the list.
     *
     * @param nbOperands the number of operands required
     * @return a list of random operators as Character objects
     */
    public List<Character> obtainOperands(int nbOperands) {
        List<Character> operators = Arrays.asList('*', '/', '-', '+');
        List<Character> operandList = new ArrayList<>();
        int countMulDiv = 0;

        for (int i = 0; i < nbOperands; i++) {
            char operator = (countMulDiv < 2) ? operators.get(random.nextInt(operators.size())) : operators.get(random.nextInt(2) + 2);
            if (operator == '*' || operator == '/') countMulDiv++;
            operandList.add(operator);
        }

        return operandList;
    }

    /**
     * Generates a random number with the specified number of digits.
     *
     * This method calculates the minimum and maximum values for the given
     * number of digits and returns a random integer within this range.
     *
     * @param numDigits the number of digits for the generated random number
     * @return a random integer with the specified number of digits
     */
    public int generateRandomNumberWithDigits(int numDigits) {
        int min = (int) Math.pow(10, numDigits - 1);
        int max = (int) Math.pow(10, numDigits) - 1;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Constructs a player response with the given answer.
     *
     * This method creates a new Response object using the provided answer
     * and returns it as an IPlayerAnswer.
     *
     * @param givenAnswer the answer provided by the player
     * @return an IPlayerAnswer representing the player's response
     */
    public IPlayerAnswer createResponse(int givenAnswer) {
        return new Response(givenAnswer);
    }
}

