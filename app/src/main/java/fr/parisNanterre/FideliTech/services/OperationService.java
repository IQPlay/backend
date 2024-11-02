package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.Operation;
import fr.parisNanterre.FideliTech.models.Response;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
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
        // Vérifie que le nombre de chiffres est positif
        if (numDigits <= 0) {
            throw new IllegalArgumentException("Le nombre de chiffres doit être supérieur à 0.");
        }
        
        // Définit le minimum comme 10^(numDigits - 1), mais garantit que le minimum est au moins 1
        int min = (int) Math.pow(10, numDigits - 1);
        int max = (int) Math.pow(10, numDigits) - 1;
    
        // Utilise random.nextInt pour générer un nombre aléatoire entre min et max
        return random.nextInt(max - min + 1) + min; // Ceci garantit que le résultat ne sera jamais 0
    }
    

    public List<Operation> createSequence3Operation(int difficulty){
        List<Operation> operations = new ArrayList<>();
        Operation o1 = createOperation(difficulty-1);
        Operation o2 = createOperation(difficulty);
        Operation o3 = createOperation(difficulty+1);
        operations.add(o1);
        operations.add(o2);
        operations.add(o3);

        return operations;

    }

    // Méthode pour comparer une réponse avec le résultat d'une opération
    public boolean isCorrectResponse(Response response, Operation operation) {
        return response.getGivenAnswer() == operation.getResult();
    }

    /**
     * Méthode pour calculer le ratio de bonnes réponses
     * @param responses Liste des réponses fournies par l'utilisateur
     * @param operations Liste des opérations à comparer
     * @return Ratio de bonnes réponses (entre 0 et 1)
     */
    public double calculateCorrectAnswerRatio(List<Response> responses, List<Operation> operations) {
        if (responses.size() != operations.size()) {
            throw new IllegalArgumentException("Les listes de réponses et d'opérations doivent avoir la même taille.");
        }

        int correctAnswers = 0;
        for (int i = 0; i < responses.size(); i++) {
            if (isCorrectResponse(responses.get(i), operations.get(i))) {
                correctAnswers++;
            }
        }

        // Calcul du ratio de bonnes réponses
        return (double) correctAnswers / responses.size();
    }


}
