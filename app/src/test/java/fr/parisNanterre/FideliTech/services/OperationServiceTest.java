package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperationServiceTest {

    private OperationService operationService;

    @BeforeEach
    public void setUp() {
        operationService = new OperationService();
    }

    @Test
    public void testObtainsOperand() {
        List<Character> operators = operationService.obtainsOperand(5);
        assertNotNull(operators);
        assertEquals(5, operators.size());

        // Vérifier que la liste contient uniquement les opérateurs valides
        for (char operator : operators) {
            assertTrue(operator == '+' || operator == '-' || operator == '*' || operator == '/');
        }
    }

    @Test
    public void testCreateOperation() {
        int difficulty = 10; // Choisir un niveau de difficulté
        Operation operation = operationService.createOperation(difficulty);

        assertNotNull(operation);
        assertNotNull(operation.getExpression());
        assertTrue(operation.getExpression().length() > 0);

        // Vérifier que le résultat est correct
        int expectedResult = evaluateExpression(operation.getExpression());
        assertEquals(expectedResult, operation.getResult());
    }

    // Méthode pour évaluer l'expression (vous pouvez choisir de l'implémenter de différentes manières)
    private int evaluateExpression(String expression) {
        // Implémentez ici une logique pour évaluer l'expression, ou utilisez une bibliothèque d'évaluation d'expressions.
        // Par exemple, vous pouvez utiliser une approche simple en utilisant une pile pour gérer les opérateurs.
        // Pour cet exemple, vous pouvez également évaluer manuellement l'expression en fonction de sa structure.
        return 0; // Placeholder
    }

    @Test
    public void testGenerateRandomNumberWithDigits() {
        int number = operationService.generateRandomNumberWithDigits(3);
        assertTrue(number >= 100 && number < 1000); // Doit être un nombre à 3 chiffres
    }
}
