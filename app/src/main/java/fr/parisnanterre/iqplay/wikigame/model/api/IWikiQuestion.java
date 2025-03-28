package fr.parisnanterre.iqplay.wikigame.model.api;

import fr.parisnanterre.iqplay.wikigame.model.Reponse;

import java.util.List;

public interface IWikiQuestion {
     String getId();
     String getWikiId();
     void setWikiId(String wikiId);
     String getUrl();
     void setUrl(String url);
     String getContent();
     void setContent(String content);
     boolean isGeneratedByAi();
     void setGeneratedByAi(boolean generatedByAi);
     String getQuestion();
     void setQuestion(String question);
     List<Reponse> getReponses();
     void setReponses(List<Reponse> reponses);
}
