package fr.parisNanterre.FideliTech.models;

public class GameCalculMental {

    // Enumération des modes de jeu
    public enum GameMode {
        ADDITION_ET_SOUSTRACTION,
        DIVISION_ET_MULTIPLICATION,
        COMBINAISON
    }

    // Attributs
    private double successTargetRate; // Taux de réussite attendu (entre 0.0 et 1.0)

    // Constructeur
    public GameCalculMental(double successTargetRate) {
        this.successTargetRate = successTargetRate;
    }

    // Getters
    public double getSuccessTargetRate() {
        return successTargetRate;
    }
}
