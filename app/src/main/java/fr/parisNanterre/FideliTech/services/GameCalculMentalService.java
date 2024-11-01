package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.GameSession;
import java.util.List;

public class GameCalculMentalService {

    private static final double DEFAULT_SUCCESS_TARGET_RATE = 0.8;
    private static final String DEFAULT_ID = "MY_SESSION_ID";
    private static final int DEFAULT_INITIAL_DIFFICULTY_LEVEL = 3;
    private static final int DEFAULT_LENGTH_OPERATIONS = 5; // Nb operations dans une séquence

    // Créer une nouvelle session de jeu
    public GameSession newSession() {
        return new GameSession(DEFAULT_ID, DEFAULT_SUCCESS_TARGET_RATE, DEFAULT_INITIAL_DIFFICULTY_LEVEL);
    }

    // Créer une opération
    
    // Créer une séquence d'opérations
    // public List<Operation> createOperationSequence(...) { ... }

    // Mettre à jour le niveau de difficulté
    // public void updateDifficultyLevel(...) { ... }

}
