package fr.parisnanterre.iqplay.dto;

public class StartGameResponse implements IDto {
    private String message;
    private Long sessionId;

    public StartGameResponse(String message, Long sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    @Override
    public String message() {
        return message;
    }

    public Long getSessionId() {
        return sessionId;
    }
}
