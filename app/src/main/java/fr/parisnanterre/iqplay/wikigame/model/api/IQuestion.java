package fr.parisnanterre.iqplay.wikigame.model.api;

import fr.parisnanterre.iqplay.wikigame.model.Reponse;

import java.util.List;

public interface IQuestion {
     String getId();
     String getIntitule();
     void setIntitule(String intitule);
     boolean isGeneratedByAi();
     void setGeneratedByAi(boolean generatedByAi);
     List<Reponse> getReponses();
     void setReponses(List<Reponse> reponses);
}
