package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.GameSession;


public class GameCalculMentalService implements IGameCalculMentalService {

    private static final double DEFAULT_SUCCESS_TARGET_RATE = 0.8;
    private static final String DEFAULT_ID = "MY_SESSION_ID";
    private static final int DEFAULT_INITIAL_DIFFICULTY_LEVEL = 1;

    @Override
    public GameSession newSession() {
        return new GameSession(DEFAULT_ID, DEFAULT_SUCCESS_TARGET_RATE, DEFAULT_INITIAL_DIFFICULTY_LEVEL);
    }

    @Override
    public void updateDifficultyLevel(double successRate, GameSession gameSession) {
        if (successRate > DEFAULT_SUCCESS_TARGET_RATE){
            gameSession.setDifficultyLevel(gameSession.getDifficultyLevel()+1);
        }
    }

}
