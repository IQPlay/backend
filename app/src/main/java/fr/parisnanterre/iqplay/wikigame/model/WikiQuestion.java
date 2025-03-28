package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IWikiDocument;
import fr.parisnanterre.iqplay.wikigame.model.api.IWikiQuestion;
import fr.parisnanterre.iqplaylib.api.IQuestion;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class WikiQuestion implements IWikiQuestion {

    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @OneToOne
    @JoinColumn(name = "wiki_document_id")
    private WikiDocument wikiDocument;

    @Override
    public String getId() {
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
