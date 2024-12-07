package fr.parisnanterre.iqplay.models;

import fr.parisnanterre.iqplaylib.api.IPlayer;

public class Player implements IPlayer {

    /**
     * Represents a player in the system, implementing the IPlayer interface.
     * Provides methods to retrieve the player's ID and name.
     */
    public Player() { }

    @Override
    public Long id() {
        return 0L;
    }
}
