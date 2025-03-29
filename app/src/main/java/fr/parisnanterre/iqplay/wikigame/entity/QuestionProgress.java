package fr.parisnanterre.iqplay.wikigame.entity;

import jakarta.persistence.*;

@Entity
public class QuestionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fiche_progress_id", nullable = false)
    private FicheProgress ficheProgress;

    @ManyToOne
    @JoinColumn(name = "wiki_question_id", nullable = false)
    private WikiQuestion wikiQuestion;

    private boolean isAnsweredCorrectly;

    public QuestionProgress() {}

    public Long getId() {
        return id;
    }

    public FicheProgress getFicheProgress() {
        return ficheProgress;
    }

    public void setFicheProgress(FicheProgress ficheProgress) {
        this.ficheProgress = ficheProgress;
    }

    public WikiQuestion getWikiQuestion() {
        return wikiQuestion;
    }

    public void setWikiQuestion(WikiQuestion wikiQuestion) {
        this.wikiQuestion = wikiQuestion;
    }

    public boolean isAnsweredCorrectly() {
        return isAnsweredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        isAnsweredCorrectly = answeredCorrectly;
    }
}

