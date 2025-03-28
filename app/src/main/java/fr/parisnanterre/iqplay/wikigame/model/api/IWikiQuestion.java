package fr.parisnanterre.iqplay.wikigame.model.api;

import fr.parisnanterre.iqplay.wikigame.model.Question;
import fr.parisnanterre.iqplay.wikigame.model.WikiDocument;

public interface IWikiQuestion {
     String getId();
     Question getQuestion();
     void setQuestion(Question question);
     WikiDocument getWikiDocument();
     void setWikiDocument(WikiDocument wikiDocument);
}
