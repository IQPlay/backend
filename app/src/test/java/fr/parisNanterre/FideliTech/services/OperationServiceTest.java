package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.Operation;
import fr.parisNanterre.FideliTech.models.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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
        // Test de la méthode obtainsOperand
        int nbOperateur = 5;
        List<Character> operands = operationService.obtainsOperand(nbOperateur);
        
        assertNotNull(operands, "The operand list should not be null.");
        assertEquals(nbOperateur, operands.size(), "The operand list should contain " + nbOperateur + " elements.");
        
        // Vérifiez que les opérateurs ne contiennent que des valeurs valides
        for (Character operand : operands) {
            assertTrue("*/+-".contains(operand.toString()), "Operand should be one of +, -, *, or /");
        }
    }

    @Test
    public void testCreateOperation_DifficultyLevel1() {
        // Test avec un faible niveau de difficulté
        int difficulty = 1;
        Operation operation = operationService.createOperation(difficulty);
        
        // Assert the operation has a valid expression and result
        assertNotNull(operation, "Operation should not be null.");
        assertTrue(operation.getExpression().length() > 0, "Expression should not be empty.");
        
        // Évaluer l'expression pour vérifier le résultat
        int expectedResult = evaluateExpression(operation.getExpression());
        assertEquals(expectedResult, operation.getResult(), "Result should match the evaluated expression.");
    }

    @Test
    public void testCreateOperation_DifficultyLevel3() {
        // Test avec un niveau de difficulté plus élevé
        int difficulty = 3;
        Operation operation = operationService.createOperation(difficulty);
        
        // Assert the operation has a valid expression and result
        assertNotNull(operation, "Operation should not be null.");
        assertTrue(operation.getExpression().length() > 0, "Expression should not be empty.");
        
        // Évaluer l'expression pour vérifier le résultat
        int expectedResult = evaluateExpression(operation.getExpression());
        assertEquals(expectedResult, operation.getResult(), "Result should match the evaluated expression.");
    }

    // Une méthode simple pour évaluer les expressions
    private int evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        int result = Integer.parseInt(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            int nextNumber = Integer.parseInt(tokens[i + 1]);
            switch (operator) {
                case "+":
                    result += nextNumber;
                    break;
                case "-":
                    result -= nextNumber;
                    break;
                case "*":
                    result *= nextNumber;
                    break;
                case "/":
                    if (nextNumber != 0) {
                        result /= nextNumber;
                    } else {
                        // Gérer la division par zéro
                        result = 0; // ou lancez une exception
                    }
                    break;
            }
        }
        return result;
    }

    @Test
    public void testIsCorrectResponse() {
        Response response = new Response(5, 1000); // Exemple de réponse
        Operation operation = new Operation("2 + 3", 5); // Exemple d'opération

        // Test correct
        assertTrue(operationService.isCorrectResponse(response, operation), "Response should be correct.");

        // Test incorrect
        response = new Response(4, 1000); // Réponse incorrecte
        assertFalse(operationService.isCorrectResponse(response, operation), "Response should be incorrect.");
    }

    @Test
    public void testCalculateCorrectAnswerRatio() {
        List<Response> responses = new ArrayList<>();
        responses.add(new Response(5, 1000));
        responses.add(new Response(10, 1000));
        responses.add(new Response(2, 1000));

        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation("2 + 3", 5)); // Correct
        operations.add(new Operation("5 + 5", 10)); // Correct
        operations.add(new Operation("4 - 2", 2)); // Correct

        double ratio = operationService.calculateCorrectAnswerRatio(responses, operations);
        assertEquals(1.0, ratio, "All responses should be correct, so ratio should be 1.0");

        // Test with one incorrect response
        responses.set(2, new Response(3, 1000)); // Incorrect response
        ratio = operationService.calculateCorrectAnswerRatio(responses, operations);
        assertEquals(2.0 / 3.0, ratio, "Two correct responses out of three should yield a ratio of 0.67");
    }

    // Test pour la méthode generateRandomNumberWithDigits
    @Test
    public void testGenerateRandomNumberWithDigits() {
        // Test pour un nombre de chiffres positif
        int number = operationService.generateRandomNumberWithDigits(3);
        assertTrue(number >= 100 && number <= 999, "Number should be a 3-digit number.");

        // Test pour un nombre de chiffres négatif
        assertThrows(IllegalArgumentException.class, () -> {
            operationService.generateRandomNumberWithDigits(0);
        }, "Should throw IllegalArgumentException for non-positive digit count.");
    }
}
