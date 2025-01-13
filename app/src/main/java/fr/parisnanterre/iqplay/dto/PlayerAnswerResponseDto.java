package fr.parisnanterre.iqplay.dto;

public class PlayerAnswerResponseDto {
    private String message;
    private boolean correct;

    public PlayerAnswerResponseDto(String message, boolean correct) {
        this.message = message;
        this.correct = correct;
    }

    // Getter et Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
