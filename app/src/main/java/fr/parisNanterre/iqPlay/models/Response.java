package fr.parisNanterre.iqPlay.models;

import fr.parisNanterre.iqPlay.models.interfaces.IResponse;

public class Response implements IResponse {
    // Attributs
    private int givenAnswer; // Réponse fournie par l'utilisateur
    private boolean isCorrect; // Indique si la réponse est correcte

    // Constructeur
    public Response(int givenAnswer, boolean isCorrect) {
        this.givenAnswer = givenAnswer;
        this.isCorrect = isCorrect;
    }

    @Override
    public int givenAnswer() {
        return givenAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
