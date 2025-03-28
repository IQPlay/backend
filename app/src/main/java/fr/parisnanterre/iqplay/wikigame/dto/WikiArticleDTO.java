package fr.parisnanterre.iqplay.wikigame.dto;

public class WikiArticleDTO {
    private Long wikiId;
    private String url;
    private String title;
    private String content;

    public WikiArticleDTO(Long wikiId, String url, String title, String content) {
        this.wikiId = wikiId;
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public Long getWikiId() {
        return wikiId;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
