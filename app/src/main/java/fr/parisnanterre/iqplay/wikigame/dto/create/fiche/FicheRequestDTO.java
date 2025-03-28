package fr.parisnanterre.iqplay.wikigame.dto.create.fiche;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class FicheRequestDTO {

    @Id
    private String id;
    @NotBlank(message = "Le titre de la fiche est obligatoire")
    private String titre;
    @NotBlank(message = "Le badge de la fiche est obligatoire")
    private String badge;
    @NotBlank(message = "La description de la fiche est obligatoire")
    private String description;
    @NotBlank(message = "Les questions de la fiche sont obligatoires")
    private List<WikiQuestionRequestDTO> wikiQuestion;

    public String getId() {
        return id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getBadge() {
        return badge;
    }
    public void setBadge(String badge) {
        this.badge = badge;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<WikiQuestionRequestDTO> getWikiQuestions() {
        return wikiQuestion;
    }
    public void setWikiQuestions(List<WikiQuestionRequestDTO> wikiQuestion) {
        this.wikiQuestion = wikiQuestion;
    }

    @Override
    public String toString() {
        return "Fiche{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", badge='" + badge + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + wikiQuestion +
                '}';
    }
}
