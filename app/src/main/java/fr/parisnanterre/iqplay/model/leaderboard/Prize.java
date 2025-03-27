package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Prize {
    private String description;
    private String name;
    private String id;
    private String imgUrl;
    private int credits;

    public Prize() { }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Prize{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", credits=" + credits +
                '}';
    }
}
