package fr.parisNanterre.iqPlay.models;

public class Operation {

    // Attributs
    private String expression; // Représentation de l'opération (par exemple "2 + 3")
    private int result; // Résultat de l'opération (en tant qu'entier)

    // Constructeur
    public Operation(String expression, int result) {
        this.expression = expression;
        this.result = result;
    }

    // Getters
    public String getExpression() {
        return expression;
    }

    public int getResult() {
        return result;
    }
}
