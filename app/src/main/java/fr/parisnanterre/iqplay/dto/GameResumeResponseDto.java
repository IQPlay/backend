package fr.parisnanterre.iqplay.dto;

public class GameResumeResponseDto {
    private String message;
    private int score;
    private String state;

    public GameResumeResponseDto(String message, int score, String state) {
        this.message = message;
        this.score = score;
        this.state = state;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}