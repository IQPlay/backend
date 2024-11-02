package fr.parisNanterre.iqPlay.models;

import fr.parisNanterre.iqPlay.models.interfaces.IResponse;

public class Response implements IResponse {

    // Attributs
    private int givenAnswer; // Réponse fournie par l'utilisateur
    // private long responseTime; // Temps pris pour répondre en millisecondes

    // Constructeur
    public Response(int givenAnswer, long responseTime) {
        this.givenAnswer = givenAnswer;
    //    this.responseTime = responseTime;
    }

    @Override
    public int givenAnswer() {
        return givenAnswer;
    }

   /*public long getResponseTime() {
        return responseTime;
    }
    */
}
