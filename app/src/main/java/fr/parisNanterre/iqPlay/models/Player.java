package fr.parisnanterre.iqplay.models;

import fr.parisnanterre.iqplaylib.api.IPlayer;

public class Player implements IPlayer {

    private String name;

    /**
     * Represents a player in the system, implementing the IPlayer interface.
     * Provides methods to retrieve the player's ID and name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
    }

    @Override
    public Long id() {
        return 0L;
    }

    @Override
    public String name() {
        return "";
    }
}
