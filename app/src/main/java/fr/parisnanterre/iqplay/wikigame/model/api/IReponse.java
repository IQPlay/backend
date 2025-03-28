package fr.parisnanterre.iqplay.wikigame.model.api;

public interface IReponse {
     String getId();
     void setId(String id);
     String getReponse();
     void setReponse(String reponse);
     boolean isCorrect();
     void setCorrect(boolean correct);
}
