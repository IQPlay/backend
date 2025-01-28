package fr.parisnanterre.iqplay.dto;

public class GamePauseResponseDto {
    private String message;
    private int score;
    private String state;

    public GamePauseResponseDto(String message, int score, String state) {
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
}