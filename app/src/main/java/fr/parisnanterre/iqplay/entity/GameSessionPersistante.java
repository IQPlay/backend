package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GameSessionPersistante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String name; // Nom du jeu ou de la session

    @Column(nullable = false)
    private int level; // Niveau de la session

    @Column(nullable = false)
    private int score; // Score de la session

    @Column(nullable = false)
    private String state; // État de la session (exemple : "STARTED", "ENDED")

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Date de création de la session

    // Constructeurs
    public GameSessionPersistante() {
        this.createdAt = LocalDateTime.now(); // Date par défaut à la création
        this.state = StateGameSessionEnum.CREATED.toString();
    }

    public GameSessionPersistante(Player player, String name, int level, int score, String state) {
        this.player = player;
        this.name = name;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", level=" + level +
                ", score=" + score +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
