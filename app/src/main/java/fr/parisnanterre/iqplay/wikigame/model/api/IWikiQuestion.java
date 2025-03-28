package fr.parisnanterre.iqplay.wikigame.model.api;

import fr.parisnanterre.iqplaylib.api.IQuestion;

public interface IWikiQuestion {
     String getId();
     IQuestion getQuestion();
     void setQuestion(IQuestion question);
     IWikiDocument getWikiDocument();
     void setWikiDocument(IWikiDocument wikiDocument);
}
