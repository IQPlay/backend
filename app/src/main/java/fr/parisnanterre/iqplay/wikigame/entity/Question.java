package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.wikigame.entity.api.IQuestion;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question implements IQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String intitule;
    private boolean isGeneratedByAi;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reponse> reponses;

    public Long getId() {
        return id;
    }

    @Override
    public String getIntitule() {
        return intitule;
    }

    @Override
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public boolean isGeneratedByAi() {
        return isGeneratedByAi;
    }

    @Override
    public void setGeneratedByAi(boolean generatedByAi) {
        isGeneratedByAi = generatedByAi;
    }
    @Override

    public List<Reponse> getReponses() {
        return reponses;
    }

    @Override
    public void setReponses(List<Reponse> reponses) {
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
