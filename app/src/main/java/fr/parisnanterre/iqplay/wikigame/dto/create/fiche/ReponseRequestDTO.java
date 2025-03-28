package fr.parisnanterre.iqplay.wikigame.dto.create.fiche;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class ReponseRequestDTO {

    @Id
    private String id;
    @NotBlank(message = "La réponse ne peut pas être vide")
    private String reponse;
    @NotBlank(message = "isCorrect ne peut pas être vide")
    private boolean isCorrect;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getReponse() {
        return reponse;
    }
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id='" + id + '\'' +
                ", reponse='" + reponse + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
