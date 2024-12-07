package fr.parisnanterre.iqplay.dto;

public class GameStopResponse implements IDto {
    private String message;
    private int score;
    private String status;

    public GameStopResponse(String message, int score, String status) {
        this.message = message;
        this.score = score;
        this.status = status;
    }

    @Override
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
