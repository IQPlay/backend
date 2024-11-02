package fr.parisNanterre.FideliTech.services;

import fr.parisNanterre.FideliTech.models.GameSession;


public class GameCalculMentalService {

    private static final double DEFAULT_SUCCESS_TARGET_RATE = 0.8;
    private static final String DEFAULT_ID = "MY_SESSION_ID";
    private static final int DEFAULT_INITIAL_DIFFICULTY_LEVEL = 1;
    

    // Créer une nouvelle session de jeu
    public GameSession newSession() {
        return new GameSession(DEFAULT_ID, DEFAULT_SUCCESS_TARGET_RATE, DEFAULT_INITIAL_DIFFICULTY_LEVEL);
    }

    
    public void updateDifficultyLevel(double successRate, GameSession gameSession){
        if (successRate > DEFAULT_SUCCESS_TARGET_RATE){
            gameSession.setDifficultyLevel(gameSession.getDifficultyLevel()+1);
        }
    }

}
