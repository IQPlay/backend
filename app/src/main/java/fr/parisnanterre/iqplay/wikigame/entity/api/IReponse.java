package fr.parisnanterre.iqplay.wikigame.entity.api;

public interface IReponse {
     Long getId();
     String getReponse();
     void setReponse(String reponse);
     boolean isCorrect();
     void setCorrect(boolean correct);
}
