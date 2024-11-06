package fr.parisNanterre.iqPlay.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    @Test
    void testEvaluateSimpleExpression() {
        // Test d'une expression simple
        String expression = "5 + 3 - 2";
        int result = Evaluator.evaluate(expression);
        assertEquals(6, result, "Évaluation de '5 + 3 - 2' devrait être 6");
    }

    @Test
    void testEvaluateWithDivision() {
        // Test d'une expression avec division qui donne un entier
        String expression = "8 / 4 + 2";
        int result = Evaluator.evaluate(expression);
        assertEquals(4, result, "Évaluation de '8 / 4 + 2' devrait être 4");
    }

    @Test
    void testEvaluateWithFloatDivision() {
        // Test d'une expression avec division flottante qui est arrondie vers le bas
        String expression = "5 / 2";
        int result = Evaluator.evaluate(expression);
        assertEquals(2, result, "Évaluation de '5 / 2' devrait être 2");
    }

    @Test
    void testEvaluateComplexExpression() {
        // Test d'une expression plus complexe
        String expression = "10 + 3 * 2 - 8 / 4";
        int result = Evaluator.evaluate(expression);
        assertEquals(14, result, "Évaluation de '10 + 3 * 2 - 8 / 4' devrait être 15");
    }

    @Test
    void testEvaluateInvalidExpression() {
        // Test d'une expression invalide pour s'assurer qu'une exception est lancée
        String expression = "5 +";
        assertThrows(IllegalArgumentException.class, () -> {
            Evaluator.evaluate(expression);
        }, "Une expression invalide devrait lancer une IllegalArgumentException");
    }
}
