package fr.parisnanterre.iqplay.wikigame.entity.api;

import fr.parisnanterre.iqplay.wikigame.entity.Question;
import fr.parisnanterre.iqplay.wikigame.entity.WikiDocument;

public interface IWikiQuestion {
     Long getId();
     Question getQuestion();
     void setQuestion(Question question);
     WikiDocument getWikiDocument();
     void setWikiDocument(WikiDocument wikiDocument);
}
