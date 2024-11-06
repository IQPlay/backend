package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.GameSession;
import fr.parisNanterre.iqPlay.models.GameCalculMental;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameCalculMentalService implements IGameCalculMentalService {

    private final GameCalculMental game;  // Objet contenant le taux de succès cible
    private final Map<String, GameSession> sessions; // Pour stocker les sessions par ID
    private static final int DEFAULT_INITIAL_DIFFICULTY_LEVEL = 1; // Niveau de difficulté par défaut

    public GameCalculMentalService() {
        // Initialisation de l’objet GameCalculMental avec un taux de succès spécifique
        this.game = new GameCalculMental(); // Exemple : taux de succès cible fixé à 0.8
        this.sessions = new HashMap<>(); // Initialiser la Map pour stocker les sessions
    }

    // Méthode pour démarrer une nouvelle session de jeu avec un niveau de difficulté initial fourni par le client
    @Override
    public GameSession newSession(int initialDifficultyLevel) {
        String sessionId = UUID.randomUUID().toString();  // Génère un ID unique pour chaque session
        GameSession newSession = new GameSession(sessionId, game, initialDifficultyLevel);
        sessions.put(sessionId, newSession); // Stocker la session dans la Map
        return newSession;
    }

    // Méthode pour démarrer une nouvelle session avec une difficulté par défaut de 1
    @Override
    public GameSession newSession() {
        return newSession(DEFAULT_INITIAL_DIFFICULTY_LEVEL); // Utilise la méthode avec paramètre pour créer une session avec difficulté 1
    }

    // Méthode pour récupérer une session par son ID
    public GameSession getSessionById(String sessionId) {
        return sessions.get(sessionId); // Retourne la session correspondante ou null si non trouvée
    }

    // Méthode pour supprimer une session
    public void removeSession(String sessionId) {
        sessions.remove(sessionId); // Méthode pour supprimer une session
    }

    @Override
    public void updateDifficultyLevel(double successRate, GameSession gameSession) {
        if (successRate > game.getSuccessTargetRate()) {
            gameSession.setDifficultyLevel(gameSession.getDifficultyLevel() + 1);
        }
    }
}
