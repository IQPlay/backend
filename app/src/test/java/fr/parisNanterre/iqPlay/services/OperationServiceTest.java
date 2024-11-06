package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.Operation;
import fr.parisNanterre.iqPlay.models.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class OperationServiceTest {

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtainOperand() {
        int nbOperateur = 5;
        List<Character> operands = operationService.obtainsOperand(nbOperateur);

        assertNotNull(operands, "The operand list should not be null.");
        assertEquals(nbOperateur, operands.size(), "The operand list should contain " + nbOperateur + " elements.");

        for (Character operand : operands) {
            assertTrue("*/+-".contains(operand.toString()), "Operand should be one of +, -, *, or /");
        }
    }

    @Test
    public void testIsCorrectResponse() {
        Operation operation = new Operation("2 + 3", 5);
        Response response = operationService.createResponse(5, operation);

        assertTrue(operationService.isCorrectResponse(response), "Response should be correct.");

        response = new Response(4, false);
        assertFalse(operationService.isCorrectResponse(response), "Response should be incorrect.");
    }

    @Test
    public void testCalculateCorrectAnswerRatio() {
        List<Response> responses = new ArrayList<>();
        responses.add(new Response(5, true));
        responses.add(new Response(10, true));
        responses.add(new Response(2, true));

        double ratio = operationService.calculateCorrectAnswerRatio(responses);
        assertEquals(1.0, ratio, "All responses should be correct, so ratio should be 1.0");

        responses.set(2, new Response(3, false));
        ratio = operationService.calculateCorrectAnswerRatio(responses);
        assertEquals(2.0 / 3.0, ratio, "Two correct responses out of three should yield a ratio of 0.67");
    }

    @Test
    public void testGenerateRandomNumberWithDigits() {
        int number = operationService.generateRandomNumberWithDigits(3);
        assertTrue(number >= 100 && number <= 999, "Number should be a 3-digit number.");

        assertThrows(IllegalArgumentException.class, () -> {
            operationService.generateRandomNumberWithDigits(0);
        }, "Should throw IllegalArgumentException for non-positive digit count.");
    }

    @Test
    public void testCreateOperationWithLowDifficulty() {
        int difficulty = 1;
        Operation operation = operationService.createOperation(difficulty);

        assertExpressionMatchesPattern(operation.getExpression(), 4, 3);
        assertEquals(operation.getResult(), Evaluator.evaluate(operation.getExpression()), 
                     "Le résultat de l'expression doit correspondre au résultat attendu.");
    }

    @Test
    public void testCreateOperationWithMediumDifficulty() {
        int difficulty = 10;
        Operation operation = operationService.createOperation(difficulty);

        assertExpressionMatchesPattern(operation.getExpression(), 4, 3);
        assertEquals(operation.getResult(), Evaluator.evaluate(operation.getExpression()), 
                     "Le résultat de l'expression doit correspondre au résultat attendu.");
    }

    @Test
    public void testCreateOperationWithHighDifficulty() {
        int difficulty = 20;
        Operation operation = operationService.createOperation(difficulty);

        assertExpressionMatchesPattern(operation.getExpression(), 5, 4);
        assertEquals(operation.getResult(), Evaluator.evaluate(operation.getExpression()), 
                     "Le résultat de l'expression doit correspondre au résultat attendu.");
    }

    private void assertExpressionMatchesPattern(String expression, int expectedNumbers, int expectedOperators) {
        long numberCount = Pattern.compile("\\d+").matcher(expression).results().count();
        assertEquals(expectedNumbers, numberCount, 
                     "L'expression doit contenir " + expectedNumbers + " nombres.");

        long operatorCount = Pattern.compile("[+\\-*/]").matcher(expression).results().count();
        assertEquals(expectedOperators, operatorCount, 
                     "L'expression doit contenir " + expectedOperators + " opérateurs.");
    }
}
