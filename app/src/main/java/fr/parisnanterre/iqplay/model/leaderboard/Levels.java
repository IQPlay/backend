package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Levels {
    private Level started;
    private List<Level> completed;

    public Level getStarted() {
        return started;
    }
    public void setStarted(Level started) {
        this.started = started;
    }
    public List<Level> getCompleted() {
        return completed;
    }
    public void setCompleted(List<Level> completed) {
        this.completed = completed;
    }
}
