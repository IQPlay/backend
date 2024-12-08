package fr.parisnanterre.iqplay.dto;

public class SubmitAnswerResponseDto {
    private String message;
    private int score;
    private String status;
    private String nextQuestion;

    public SubmitAnswerResponseDto(String message, int score, String status, String nextQuestion) {
        this.message = message;
        this.score = score;
        this.status = status;
        this.nextQuestion = nextQuestion;
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

    public String getNextQuestion() {
        return nextQuestion;
    }
}
