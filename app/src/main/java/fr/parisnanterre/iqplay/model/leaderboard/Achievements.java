package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Achievements {
    private List<Achievement> started;
    private List<Achievement> completed;

    public List<Achievement> getStarted() {
        return started;
    }

    public List<Achievement> getCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return "Achievements{" +
                "started=" + started +
                ", completed=" + completed +
                '}';
    }
}
