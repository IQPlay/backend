package fr.parisnanterre.iqplay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.parisnanterre.iqplay.entity.Player;

import java.time.LocalDateTime;

public class HistoryGameResponseDto {

    private Long id;

    private String name;

    private int level;

    private int score;

    private String state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    public HistoryGameResponseDto(Long id, String name, int level, int score, String state, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.score = score;
        this.state = state;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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
}
