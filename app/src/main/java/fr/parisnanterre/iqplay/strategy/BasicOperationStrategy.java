package fr.parisnanterre.iqplay.strategy;

import fr.parisnanterre.iqplay.api.IOperationStrategy;
import fr.parisnanterre.iqplay.service.Evaluator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class BasicOperationStrategy implements IOperationStrategy {

    private final Random random = new Random();

    @Override
    public String generateExpression(int difficulty) {
        List<Character> operands = obtainOperands(difficulty % 3 + 2);
        List<Integer> numbers = generateRandomNumbers(operands.size() + 1, 1 + difficulty / 10);

        StringBuilder expression = new StringBuilder();
        expression.append(numbers.get(0));
        for (int i = 0; i < operands.size(); i++) {
            expression.append(" ").append(operands.get(i)).append(" ").append(numbers.get(i + 1));
        }
        return expression.toString();
    }

    @Override
    public int evaluate(String expression) {
        return Evaluator.evaluate(expression);
    }

    private List<Character> obtainOperands(int nbOperands) {
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

    private List<Integer> generateRandomNumbers(int count, int numDigits) {
        int min = (int) Math.pow(10, numDigits - 1);
        int max = (int) Math.pow(10, numDigits) - 1;
        return random.ints(count, min, max + 1).boxed().toList();
    }
}
