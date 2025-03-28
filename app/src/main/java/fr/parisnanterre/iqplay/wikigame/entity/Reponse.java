package fr.parisnanterre.iqplay.wikigame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.parisnanterre.iqplay.wikigame.entity.api.IReponse;
import jakarta.persistence.*;

@Entity
public class Reponse implements IReponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reponse;
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore // Empêche la récursion JSON
    private Question question;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id + // correction ici pour ne pas avoir des guillemets autour
                ", reponse='" + reponse + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
