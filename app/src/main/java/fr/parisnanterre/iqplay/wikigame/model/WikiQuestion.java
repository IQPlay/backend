package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IWikiDocument;
import fr.parisnanterre.iqplay.wikigame.model.api.IWikiQuestion;
import fr.parisnanterre.iqplaylib.api.IQuestion;

import java.util.List;

public class WikiQuestion implements IWikiQuestion {

    private String id;
    private IQuestion question;
    private IWikiDocument wikiDocument;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IQuestion getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(IQuestion question) {
        this.question = question;
    }

    @Override
    public IWikiDocument getWikiDocument() {
        return wikiDocument;
    }

    @Override
    public void setWikiDocument(IWikiDocument wikiDocument) {
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
