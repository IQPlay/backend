package fr.parisnanterre.iqplay.model.leaderboard.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.parisnanterre.iqplay.model.leaderboard.Missions;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionsWrapper {
    private Missions missions;

    public Missions getMissions() { return missions; }
    public void setMissions(Missions missions) { this.missions = missions; }
}
