package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IFiche;
import fr.parisnanterre.iqplay.wikigame.model.api.IWikiQuestion;
import jakarta.persistence.Id;

public class Fiche implements IFiche {

    @Id
    private String id;
    private String titre;
    private String badge;
    private String description;
    private IWikiQuestion wikiQuestion;

    @Override
    public String getId() {
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
    public IWikiQuestion getWikiQuestion() {
        return wikiQuestion;
    }

    @Override
    public void setWikiQuestion(IWikiQuestion wikiQuestion) {
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
