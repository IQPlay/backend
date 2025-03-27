package fr.parisnanterre.iqplay.model.leaderboard.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.parisnanterre.iqplay.model.leaderboard.Achievements;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementsWrapper {
    private Achievements achievements;

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }
}
