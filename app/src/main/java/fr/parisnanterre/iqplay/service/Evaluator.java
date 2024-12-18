package fr.parisnanterre.iqplay.service;

import org.apache.commons.jexl3.*;

public class Evaluator {

    /**
     * Evaluates a mathematical expression provided as a string and returns the result as an integer.
     *
     * This method uses the JEXL library to parse and evaluate the expression. It supports expressions
     * that result in either integer or double values, converting double results to integers by truncation.
     *
     * @param expression the mathematical expression to evaluate
     * @return the integer result of the evaluated expression
     * @throws IllegalArgumentException if the expression is invalid or results in an unexpected type
     */
    public static int evaluate(String expression) {
        JexlEngine jexl = new JexlBuilder().create();

        try {
            JexlExpression e = jexl.createExpression(expression);
            Object result = e.evaluate(null);

            if (result instanceof Double) {
                return ((Double) result).intValue();
            } else if (result instanceof Integer) {
                return (Integer) result;
            } else {
                throw new IllegalArgumentException("Unexpected result type: " + result.getClass());
            }
        } catch (JexlException e) {
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }
    }
}

