package fr.parisnanterre.iqplay.wikigame.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.parisnanterre.iqplay.wikigame.entity.api.IWikiQuestion;
import jakarta.persistence.*;

@Entity
public class WikiQuestion implements IWikiQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Question question;

    @OneToOne(cascade = CascadeType.ALL)
    private WikiDocument wikiDocument;

    @ManyToOne
    @JoinColumn(name = "fiche_id")
    @JsonBackReference
    private Fiche fiche;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public WikiDocument getWikiDocument() {
        return wikiDocument;
    }

    @Override
    public void setWikiDocument(WikiDocument wikiDocument) {
        this.wikiDocument = wikiDocument;
    }

    @Override
    public String toString() {
        return "WikiQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}
