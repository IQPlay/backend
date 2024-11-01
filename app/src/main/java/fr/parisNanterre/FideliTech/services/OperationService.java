package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.Operation;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OperationService {

    private Random random = new Random();

    // Méthode pour obtenir une liste d'opérateurs
    public List<Character> obtainsOperand(int nbOperateur) {
        List<Character> operators = Arrays.asList('*', '/', '-', '+');
        List<Character> operandList = new ArrayList<>();
        int countMulDiv = 0;
        char operator;

        // Générer des opérateurs aléatoirement
        for (int i = 0; i < nbOperateur; i++) {
            if (countMulDiv < 2) {
                // Choisir parmi tous les opérateurs
                operator = operators.get(random.nextInt(operators.size()));
            } else {
                // Choisir uniquement entre '-' et '+'
                operator = operators.get(random.nextInt(2) + 2); // Indices 2 et 3 pour '-' et '+'
            }

            if (operator == '*' || operator == '/') {
                countMulDiv++;
            }

            operandList.add(operator);
        }
        return operandList;
    }

    // Méthode pour créer une opération
    public Operation createOperation(int difficulty) {
        // Obtenir les opérateurs
        List<Character> operands = obtainsOperand(difficulty % 3 + 2);

        // Calculer les nombres de chiffres requis pour chaque nombre
        int[] digits = new int[4];
        for (int i = 0; i < 4; i++) {
            digits[i] = 1 + difficulty / 15;
            if (difficulty % 15 >= 3 + 3 * i) {
                digits[i] += 1;
            }
        }

        // Générer les nombres aléatoires selon le nombre de chiffres requis
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < operands.size() + 1; i++) {
            int numDigits = digits[i];
            int number = generateRandomNumberWithDigits(numDigits);
            numbers.add(number);
        }

        // Construire l'expression et calculer le résultat
        StringBuilder expressionBuilder = new StringBuilder();
        int result = numbers.get(0);
        expressionBuilder.append(result);

        for (int i = 0; i < operands.size(); i++) {
            char operand = operands.get(i);
            int nextNumber = numbers.get(i + 1);

            expressionBuilder.append(" ").append(operand).append(" ").append(nextNumber);

            // Calculer le résultat en fonction de l'opérateur
            switch (operand) {
                case '+':
                    result += nextNumber;
                    break;
                case '-':
                    result -= nextNumber;
                    break;
                case '*':
                    result *= nextNumber;
                    break;
                case '/':
                    if (nextNumber != 0) {
                        result /= nextNumber;
                    } else {
                        result = 0; // Gérer la division par zéro
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Opérateur non supporté : " + operand);
            }
        }

        // Créer l'objet Operation avec l'expression et le résultat calculé
        String expression = expressionBuilder.toString();
        return new Operation(expression, result);
    }

    // Méthode pour générer un nombre aléatoire avec un nombre spécifique de chiffres
    public int generateRandomNumberWithDigits(int numDigits) {
        int min = (int) Math.pow(10, numDigits - 1);
        int max = (int) Math.pow(10, numDigits) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
