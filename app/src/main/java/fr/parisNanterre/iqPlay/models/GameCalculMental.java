package fr.parisNanterre.iqPlay.models;

public class GameCalculMental {
    // Attribut pour le taux de réussite attendu (entre 0.0 et 1.0)
    private static final double DEFAULT_SUCCESS_TARGET_RATE = 0.0; // Taux de succès fixe par défaut

    private double successTargetRate; // Taux de réussite pour cette instance

    // Constructeur
    public GameCalculMental() {
        this.successTargetRate = DEFAULT_SUCCESS_TARGET_RATE; // Initialise avec le taux de succès par défaut
    }

    // Getter pour obtenir le taux de réussite
    public double getSuccessTargetRate() {
        return successTargetRate;
    }
}
