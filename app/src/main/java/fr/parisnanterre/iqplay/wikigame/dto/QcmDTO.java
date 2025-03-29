package fr.parisnanterre.iqplay.wikigame.dto;

import java.util.List;

public class QcmDTO {
    private String content;
    private List<QuestionDTO> qcm;

    public QcmDTO() {}

    public QcmDTO(String content, List<QuestionDTO> qcm) {
        this.content = content;
        this.qcm = qcm;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<QuestionDTO> getQcm() {
        return qcm;
    }

    public void setQcm(List<QuestionDTO> qcm) {
        this.qcm = qcm;
    }
}
