package fr.parisnanterre.iqplay.wikigame.dto;


public class ResponseDTO {
    private String reponse;
    private boolean correct;

    public ResponseDTO() {}

    public ResponseDTO(String reponse, boolean correct) {
        this.reponse = reponse;
        this.correct = correct;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
