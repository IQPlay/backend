package fr.parisNanterre.FideliTech.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class CalculMentalController {

    private Random random = new Random();
    private int operand1;
    private int operand2;
    private String operator;

    @GetMapping("/calcul")
    public String getCalcul() {
        operand1 = random.nextInt(100);
        operand2 = random.nextInt(100);
        operator = randomOperator();

        return operand1 + " " + operator + " " + operand2 + " = ?";
    }

    @PostMapping("/verifier")
    public boolean verifierResultat(@RequestParam("resultat") int resultat) {
        int correctResult = calculateResult(operand1, operand2, operator);
        return resultat == correctResult;
    }

    private String randomOperator() {
        String[] operators = {"+", "-", "*", "/"};
        return operators[random.nextInt(operators.length)];
    }

    private int calculateResult(int operand1, int operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand2 != 0 ? operand1 / operand2 : 0;
            default:
                return 0;
        }
    }
}
