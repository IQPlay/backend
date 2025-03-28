package fr.parisnanterre.iqplay.wikigame.model.api;

public interface IFiche {
     String getId();
     String getTitre();
     void setTitre(String titre);
     String getBadge();
     void setBadge(String badge);
     String getDescription();
     void setDescription(String description);
     IWikiQuestion getWikiQuestion();
     void setWikiQuestion(IWikiQuestion wikiQuestion);
}
