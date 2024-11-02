package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.GameSession;

public interface IGameCalculMentalService {

    // Créer une nouvelle session de jeu
    GameSession newSession();

    public void updateDifficultyLevel(double successRate, GameSession gameSession);
}
