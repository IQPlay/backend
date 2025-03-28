package fr.parisnanterre.iqplay.wikigame.entity.api;

import fr.parisnanterre.iqplay.wikigame.entity.WikiQuestion;

import java.util.List;

public interface IFiche {
     String getId();
     String getTitre();
     void setTitre(String titre);
     String getBadge();
     void setBadge(String badge);
     String getDescription();
     void setDescription(String description);
     List<WikiQuestion> getWikiQuestions();
     void setWikiQuestions(List<WikiQuestion> wikiQuestion);
}
