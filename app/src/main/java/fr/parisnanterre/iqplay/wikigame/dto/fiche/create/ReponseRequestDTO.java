package fr.parisnanterre.iqplay.wikigame.dto.fiche.create;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ReponseRequestDTO {

    @Id
    private String id;
    @NotBlank(message = "La réponse ne peut pas être vide")
    private String reponse;
    @NotEmpty
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
