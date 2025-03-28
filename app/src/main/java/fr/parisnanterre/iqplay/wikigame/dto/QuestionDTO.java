package fr.parisnanterre.iqplay.wikigame.dto;

import java.util.List;

public class QuestionDTO {
    private String question;
    private List<ResponseDTO> reponses;

    public QuestionDTO() {}

    public QuestionDTO(String question, List<ResponseDTO> reponses) {
        this.question = question;
        this.reponses = reponses;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ResponseDTO> getReponses() {
        return reponses;
    }

    public void setReponses(List<ResponseDTO> reponses) {
        this.reponses = reponses;
    }
}
