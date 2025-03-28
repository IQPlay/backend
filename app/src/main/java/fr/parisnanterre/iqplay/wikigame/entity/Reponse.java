package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.wikigame.entity.api.IReponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Reponse implements IReponse {

    @Id
    private Long id;
    private String reponse;
    private boolean isCorrect;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getReponse() {
        return reponse;
    }

    @Override
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    @Override
    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
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
