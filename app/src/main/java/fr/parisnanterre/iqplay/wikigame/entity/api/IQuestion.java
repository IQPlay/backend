package fr.parisnanterre.iqplay.wikigame.entity.api;

import fr.parisnanterre.iqplay.wikigame.entity.Reponse;

import java.util.List;

public interface IQuestion {
     Long getId();
     String getIntitule();
     void setIntitule(String intitule);
     boolean isGeneratedByAi();
     void setGeneratedByAi(boolean generatedByAi);
     List<Reponse> getReponses();
     void setReponses(List<Reponse> reponses);
}
