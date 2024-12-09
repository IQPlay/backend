package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplaylib.api.ICorrectAnswer;
import fr.parisnanterre.iqplaylib.api.IQuestion;
import fr.parisnanterre.iqplaylib.core.CorrectAnswer;

/**
 * Represents an operation with an expression and its result.
 * Implements the IQuestion interface to provide the question
 * and its correct answer.
 */
public class Operation implements IQuestion {
    private String expression;
    private int result;

    /**
     * Constructs an Operation with the specified expression and result.
     *
     * @param expression the mathematical expression of the operation
     * @param result the result of the operation
     */
    public Operation(String expression, int result) {
        this.expression = expression;
        this.result = result;
    }

    /**
     * Returns the mathematical expression of the operation.
     *
     * @return the expression as a String
     */
    @Override
    public String question() {
        return expression;
    }

    /**
     * Returns the correct answer for the operation.
     *
     * @return an ICorrectAnswer object containing the result of the operation as a String
     */
    @Override
    public ICorrectAnswer correctAnswer() {
        return new CorrectAnswer(String.valueOf(result));
    }
}
