package fr.parisnanterre.iqplay.dto;

public class StartGameResponseDto {
    private String message;
    private Long sessionId;

    public StartGameResponseDto(String message, Long sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public String message() {
        return message;
    }

    public Long getSessionId() {
        return sessionId;
    }
}
