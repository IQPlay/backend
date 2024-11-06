package fr.parisNanterre.iqPlay.models;

import java.util.List;

public class GameSession {
    // Attributs
    private String SessionID;
    private double successTargetRate; // Taux de réussite attendu pour le jeu
    private int difficultyLevel; // Niveau de difficulté pour cette session
    private List<Operation> currentOperations; // Liste des opérations de la dernière séquence
    private List<Response> currentResponses; // Liste des réponses de la dernière séquence

    // Constructeur
    public GameSession(String SessionID, GameCalculMental game, int initialDifficultyLevel) {
        this.SessionID=SessionID;
        this.successTargetRate = game.getSuccessTargetRate();
        this.difficultyLevel = initialDifficultyLevel;
        this.currentOperations = null; // Initialisation à null, la séquence sera créée plus tard
        this.currentResponses = null;
    }

    // Getters
    public String getSessionID() {
        return SessionID;
    }
    public double getSuccessTargetRate() {
        return successTargetRate;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public List<Operation> getCurrentOperations() {
        return currentOperations;
    }
    public List<Response> getCurrentResponses() {
        return currentResponses;
    }

    // Méthode pour mettre à jour
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setCurrentOperations(List<Operation> operations) {
        this.currentOperations = operations;
    }

    public void setCurrentResponses(List<Response> responses){
        this.currentResponses = responses;
    }
    
}
