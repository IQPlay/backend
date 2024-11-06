package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.GameSession;

public interface IGameCalculMentalService {

    // Créer une nouvelle session de jeu
    GameSession newSession();
    GameSession newSession(int initialDifficultyLevel);

    public void updateDifficultyLevel(double successRate, GameSession gameSession);
    
}
