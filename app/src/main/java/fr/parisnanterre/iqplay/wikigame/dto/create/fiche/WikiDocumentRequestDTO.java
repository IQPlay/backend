package fr.parisnanterre.iqplay.wikigame.dto.create.fiche;

import fr.parisnanterre.iqplay.wikigame.entity.api.IWikiDocument;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class WikiDocumentRequestDTO implements IWikiDocument {

    @Id
    private Long id;
    @NotBlank(message = "URL ne peut pas être vide")
    private String url;
    @NotBlank(message = "Content ne peut pas être vide")
    private String content;
    @NotBlank(message = "Title ne peut pas être vide")
    private String title;

    public Long getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "WikiDocument{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
