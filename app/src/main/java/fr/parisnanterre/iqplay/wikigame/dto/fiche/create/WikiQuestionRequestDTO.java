package fr.parisnanterre.iqplay.wikigame.dto.fiche.create;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class WikiQuestionRequestDTO {

    @Id
    private String id;
    @NotBlank(message = "Question ne peut pas être vide")
    private QuestionRequestDTO question;
    @NotBlank(message = "WikiDocument ne peut pas être vide")
    private WikiDocumentRequestDTO wikiDocument;

    public String getId() {
        return id;
    }
    public QuestionRequestDTO getQuestion() {
        return question;
    }
    public void setQuestion(QuestionRequestDTO question) {
        this.question = question;
    }
    public WikiDocumentRequestDTO getWikiDocument() {
        return wikiDocument;
    }
    public void setWikiDocument(WikiDocumentRequestDTO wikiDocument) {
        this.wikiDocument = wikiDocument;
    }

    @Override
    public String toString() {
        return "WikiQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}
