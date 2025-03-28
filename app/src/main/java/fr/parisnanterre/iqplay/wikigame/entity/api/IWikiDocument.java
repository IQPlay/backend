package fr.parisnanterre.iqplay.wikigame.entity.api;

public interface IWikiDocument {
    Long getId();
    String getTitle();
    void setTitle(String title);
    String getContent();
    void setContent(String content);
    String getUrl();
    void setUrl(String url);
    String getWikiId();
    void setWikiId(String wikiId);
}
