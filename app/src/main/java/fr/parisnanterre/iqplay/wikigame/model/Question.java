package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IQuestion;

import java.util.List;

public class Question implements IQuestion {
    private String id;
    private String intitule;
    private boolean isGeneratedByAi;
    private List<Reponse> reponses;

    public String getId() {
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
