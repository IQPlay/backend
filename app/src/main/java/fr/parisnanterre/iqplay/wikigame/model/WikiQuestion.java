package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IWikiQuestion;

import java.util.List;

public class WikiQuestion implements IWikiQuestion {
    private String id;
    private String wikiId;
    private String url;
    private String content;
    private boolean isGeneratedByAi;
    private String question;
    private List<Reponse> reponses;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getWikiId() {
        return wikiId;
    }

    @Override
    public void setWikiId(String wikiId) {
        this.wikiId = wikiId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
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
    public String getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(String question) {
        this.question = question;
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
        return "WikiQuestion{" +
                "id='" + id + '\'' +
                ", wikiId='" + wikiId + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", isGeneratedByAi=" + isGeneratedByAi +
                ", question='" + question + '\'' +
                ", reponses=" + reponses +
                '}';
    }
}
