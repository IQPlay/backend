package fr.parisnanterre.iqplay.model.leaderboard.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.parisnanterre.iqplay.model.leaderboard.Levels;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevelWrapper {
    private Levels levels;

    public Levels getLevels() {
        return levels;
    }

    public void setLevels(Levels levels) {
        this.levels = levels;
    }
}
