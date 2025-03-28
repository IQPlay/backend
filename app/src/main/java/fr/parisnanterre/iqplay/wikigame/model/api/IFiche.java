package fr.parisnanterre.iqplay.wikigame.model.api;

import fr.parisnanterre.iqplay.wikigame.model.WikiQuestion;

import java.util.List;

public interface IFiche {
     String getId();
     String getTitre();
     void setTitre(String titre);
     String getBadge();
     void setBadge(String badge);
     String getDescription();
     void setDescription(String description);
     List<WikiQuestion> getQuestions();
     void setQuestions(List<WikiQuestion> questions);
}
