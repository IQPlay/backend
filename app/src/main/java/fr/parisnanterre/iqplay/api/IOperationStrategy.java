package fr.parisnanterre.iqplay.api;

public interface IOperationStrategy {
    String generateExpression(int difficulty);
    int evaluate(String expression);
}
