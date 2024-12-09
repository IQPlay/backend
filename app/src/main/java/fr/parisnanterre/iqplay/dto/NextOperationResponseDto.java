package fr.parisnanterre.iqplay.dto;

public class NextOperationResponseDto {
    private String message;
    private String question;
    private String status;

    public NextOperationResponseDto(String message, String question, String status) {
        this.message = message;
        this.question = question;
        this.status = status;
    }

    public String message() {
        return message;
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
    }
}
