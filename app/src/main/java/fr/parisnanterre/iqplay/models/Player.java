package fr.parisnanterre.iqplay.models;

import fr.parisnanterre.iqplaylib.api.IPlayer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player implements IPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents a player in the system, implementing the IPlayer interface.
     * Provides methods to retrieve the player's ID and name.
     */
    public Player() { }

    @Override
    public Long id() { return this.id; }
}
