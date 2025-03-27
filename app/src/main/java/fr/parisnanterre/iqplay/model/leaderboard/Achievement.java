package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Achievement {
    private String id;
    private String name;
    private String description;

    @JsonProperty("isAvailable")
    private boolean available;

    private int steps;
    private String status;
    private int count;
    private Actions actions;
    private Reward reward;

    public Achievement() { }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getSteps() {
        return steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public Actions getActions() {
        return actions;
    }
    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public Reward getReward() {
        return reward;
    }
    public void setReward(Reward reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", steps=" + steps +
                ", status='" + status + '\'' +
                ", count=" + count +
                ", actions=" + actions +
                ", reward=" + reward +
                '}';
    }
}

