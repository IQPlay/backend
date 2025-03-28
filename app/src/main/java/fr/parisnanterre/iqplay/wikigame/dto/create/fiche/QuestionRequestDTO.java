package fr.parisnanterre.iqplay.wikigame.dto.create.fiche;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class QuestionRequestDTO {
    @Id
    private String id;
    @NotBlank(message = "L'intitulé de la question ne peut pas être vide")
    private String intitule;
    private boolean isGeneratedByAi;
    @NotBlank(message = "La liste des réponses ne peut pas être vide")
    private List<ReponseRequestDTO> reponses;

    public String getId() {
        return id;
    }
    public String getIntitule() {
        return intitule;
    }
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
    public boolean isGeneratedByAi() {
        return isGeneratedByAi;
    }
    public void setGeneratedByAi(boolean generatedByAi) {
        isGeneratedByAi = generatedByAi;
    }
    public List<ReponseRequestDTO> getReponses() {
        return reponses;
    }
    public void setReponses(List<ReponseRequestDTO> reponses) {
        this.reponses = reponses;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", intitule='" + intitule + '\'' +
                ", isGeneratedByAi=" + isGeneratedByAi +
                ", reponses=" + reponses +
                '}';
    }
}
