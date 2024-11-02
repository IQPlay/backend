package fr.parisNanterre.FideliTech.models;

public class Response {

    // Attributs
    private int givenAnswer; // Réponse fournie par l'utilisateur
    // private long responseTime; // Temps pris pour répondre en millisecondes

    // Constructeur
    public Response(int givenAnswer, long responseTime) {
        this.givenAnswer = givenAnswer;
    //    this.responseTime = responseTime;
    }

    // Getters
    public int getGivenAnswer() {
        return givenAnswer;
    }

   /*public long getResponseTime() {
        return responseTime;
    }
    */
}
