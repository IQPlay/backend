package fr.parisNanterre.FideliTech.models;

public class GameCalculMental {
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
