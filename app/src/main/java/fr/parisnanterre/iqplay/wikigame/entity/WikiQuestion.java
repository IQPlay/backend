package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.wikigame.entity.api.IWikiQuestion;
import jakarta.persistence.*;

@Entity
public class WikiQuestion implements IWikiQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @OneToOne
    @JoinColumn(name = "wiki_document_id")
    private WikiDocument wikiDocument;

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
