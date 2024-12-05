package fr.parisNanterre.iqPlay.services;

import org.apache.commons.jexl3.*;

public class Evaluator {

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
