package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.wikigame.entity.api.IFiche;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Fiche implements IFiche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String badge;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WikiQuestion> wikiQuestions;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitre() {
        return titre;
    }

    @Override
    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String getBadge() {
        return badge;
    }

    @Override
    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<WikiQuestion> getWikiQuestions() {
        return wikiQuestions;
    }

    @Override
    public void setWikiQuestions(List<WikiQuestion> wikiQuestion) {
        this.wikiQuestions = wikiQuestion;
    }

    @Override
    public String toString() {
        return "Fiche{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", badge='" + badge + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + wikiQuestions +
                '}';
    }
}
