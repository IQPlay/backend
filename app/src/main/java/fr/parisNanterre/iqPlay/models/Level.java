package fr.parisnanterre.iqplay.models;

import fr.parisnanterre.iqplaylib.api.ILevel;

/**
 * Represents a level in a game, implementing the ILevel interface.
 *
 * <p>This class manages the current level state, allowing initialization,
 * incrementing, and decrementing of the level. The level cannot be decremented
 * below 1.</p>
 *
 * <p>Methods:</p>
 * <ul>
 *   <li>{@code init()}: Resets the level to 1.</li>
 *   <li>{@code level()}: Returns the current level.</li>
 *   <li>{@code levelUp()}: Increments the current level by 1.</li>
 *   <li>{@code levelDown()}: Decrements the current level by 1, but not below 1.</li>
 * </ul>
 */
public class Level implements ILevel {
    private int currentLevel = 1;

    @Override
    public void init() {
        currentLevel = 1;
    }

    @Override
    public int level() {
        return currentLevel;
    }

    @Override
    public void levelUp() {
        currentLevel++;
    }

    @Override
    public void levelDown() {
        if (currentLevel > 1) {
            currentLevel--;
        }
    }
}
