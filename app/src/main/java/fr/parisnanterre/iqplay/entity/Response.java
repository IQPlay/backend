package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplaylib.api.IPlayerAnswer;

/**
 * Represents a player's response in the game.
 * Implements the IPlayerAnswer interface to provide the given answer as a string.
 */
public class Response implements IPlayerAnswer {
    private int givenAnswer;

    /**
     * Constructs a Response object with the specified answer.
     *
     * @param givenAnswer the answer provided by the player
     */
    public Response(int givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    /**
     * Returns the player's given answer as a string.
     *
     * @return the answer provided by the player, converted to a string
     */
    @Override
    public String answer() {
        return String.valueOf(givenAnswer);
    }
}
