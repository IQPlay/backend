package fr.parisnanterre.iqplay.models;

import fr.parisnanterre.iqplaylib.api.IScore;

/**
 * Represents a score with functionality to retrieve and increment its value.
 * Implements the IScore interface.
 */
public class Score implements IScore {
    private int value = 0;

    /**
     * Retrieves the current score value.
     *
     * @return the current score as an integer.
     */
    @Override
    public int score() {
        return value;
    }

    /**
     * Increments the current score by the specified value.
     *
     * @param increment the amount to add to the current score.
     */
    @Override
    public void incrementScore(int increment) {
        value += increment;
    }
}
