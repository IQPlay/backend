package fr.parisnanterre.iqplay.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import fr.parisnanterre.iqplaylib.api.IPlayer;

@Entity
public class GameSessionPersistante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémente l'ID
    private Long id;

    @ManyToOne(optional = false) // Relation Many-to-One avec Player
    @JoinColumn(name = "player_id", nullable = false)
    private IPlayer player;

    private int level; // Niveau de la session
    private int score; // Score de la session
    private String state; // État de la session (exemple : "STARTED", "ENDED")

    private LocalDateTime createdAt; // Date de création de la session

    // Constructeurs
    public GameSessionPersistante() {}

    public GameSessionPersistante(Player player, int level, int score, String state) {
        this.player = player;
        this.level = level;
        this.score = score;
        this.state = state;
        this.createdAt = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GameSessionPersistante{" +
                "id=" + id +
                ", player=" + player +
                ", level=" + level +
                ", score=" + score +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
