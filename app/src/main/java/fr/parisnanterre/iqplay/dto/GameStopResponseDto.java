package fr.parisnanterre.iqplay.dto;

public class GameStopResponseDto {
    private String message;
    private int score;
    private String status;

    public GameStopResponseDto(String message, int score, String status) {
        this.message = message;
        this.score = score;
        this.status = status;
    }

    public String message() {
        return message;
    }

    public int getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }
}
