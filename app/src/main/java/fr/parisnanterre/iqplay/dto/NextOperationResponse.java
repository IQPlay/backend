package fr.parisnanterre.iqplay.dto;

public class NextOperationResponse implements IDto {
    private String message;
    private String question;
    private String status;

    public NextOperationResponse(String message, String question, String status) {
        this.message = message;
        this.question = question;
        this.status = status;
    }

    @Override
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
