package fr.parisnanterre.iqplay.wikigame.model;

import fr.parisnanterre.iqplay.wikigame.model.api.IWikiDocument;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WikiDocument implements IWikiDocument {
    @Id
    private Long id;
    private String url;
    private String content;
    private String title;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
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
