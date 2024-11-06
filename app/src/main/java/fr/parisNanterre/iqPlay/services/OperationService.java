package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.Operation;
import fr.parisNanterre.iqPlay.models.Response;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class OperationService implements IOperationService {

    private Random random = new Random();
    private static final int DEFAULT_SEQUENCE_SIZE = 3;

    @Override
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


    @Override
    public Operation createOperation(int difficulty) {
        // Obtenir les opérateurs
        List<Character> operands = obtainsOperand(difficulty % 3 + 2);

        // Calculer les nombres de chiffres requis pour chaque nombre
        int[] digits = new int[operands.size() + 1];
        for (int i = 0; i < digits.length; i++) {
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

        // Construire l'expression sous forme de chaîne
        StringBuilder expressionBuilder = new StringBuilder();
        expressionBuilder.append(numbers.get(0));  // Commencer avec le premier nombre

        // Ajouter les opérateurs et les nombres dans l'expression
        for (int i = 0; i < operands.size(); i++) {
            expressionBuilder.append(" ").append(operands.get(i))
                              .append(" ").append(numbers.get(i + 1));
        }

        // Récupérer l'expression sous forme de chaîne
        String expression = expressionBuilder.toString();

        // Évaluer l'expression
        int result = Evaluator.evaluate(expression);

        // Créer l'objet Operation avec l'expression et le résultat
        return new Operation(expression, result);
    }

    
    
    

    @Override
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

    @Override
    public List<Operation> createSequenceNOperation(int difficulty ) {
        List<Operation> operations = new ArrayList<>();
    
        // Calculer la difficulté de chaque opération pour qu'elles soient autour de la difficulté médiane
        for (int i = 0; i < DEFAULT_SEQUENCE_SIZE ; i++) {
            int currentDifficulty = difficulty + (i - DEFAULT_SEQUENCE_SIZE / 2); // Crée des difficultés autour de 'difficulty'
            Operation operation = createOperation(currentDifficulty);
            operations.add(operation);
        }
    
        return operations;
    }

    public Response createResponse(int givenAnswer, Operation operation) {
        boolean isCorrect = (givenAnswer == operation.getResult());
        return new Response(givenAnswer, isCorrect);
    }

    public boolean isCorrectResponse(Response reponse){
        return reponse.isCorrect();
    }

    @Override
    public double calculateCorrectAnswerRatio(List<Response> responses) {
        int correctAnswers = 0;
        for (Response response : responses) {
            if (response.isCorrect()) {
                correctAnswers++;
            }
        }
    
        // Calcul du ratio de bonnes réponses
        return (double) correctAnswers / responses.size();
    }
    

}
