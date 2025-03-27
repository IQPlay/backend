package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Missions {
    private List<Mission> started;
    private List<Mission> completed;

    public List<Mission> getStarted() {
        return started;
    }

    public void setStarted(List<Mission> started) {
        this.started = started;
    }

    public List<Mission> getCompleted() {
        return completed;
    }

    public void setCompleted(List<Mission> completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Missions{" +
                "started=" + started +
                ", completed=" + completed +
                '}';
    }
}
