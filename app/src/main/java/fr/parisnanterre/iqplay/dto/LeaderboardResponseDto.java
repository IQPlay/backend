package fr.parisnanterre.iqplay.dto;

import fr.parisnanterre.iqplay.model.leaderboard.Achievement;
import fr.parisnanterre.iqplay.model.leaderboard.Level;
import fr.parisnanterre.iqplay.model.leaderboard.Mission;
import fr.parisnanterre.iqplay.model.leaderboard.Prize;

import java.util.List;

public class GamelayerEntitiesResponseDto {

    private List<Mission> startedMissions;
    private List<Mission> completedMissions;
    private List<Achievement> startedAchievements;
    private List<Achievement> completedAchievements;
    private Level highestLevel;
    private List<Prize> prizes;

    public GamelayerEntitiesResponseDto(List<Mission> startedMissions, List<Mission> completedMissions,
                                        List<Achievement> startedAchievements, List<Achievement> completedAchievements,
                                        Level highestLevel, List<Prize> prizes) {
        this.startedMissions = startedMissions;
        this.completedMissions = completedMissions;
        this.startedAchievements = startedAchievements;
        this.completedAchievements = completedAchievements;
        this.highestLevel = highestLevel;
        this.prizes = prizes;
    }

    public List<Mission> getStartedMissions() { return startedMissions; }
    public List<Mission> getCompletedMissions() { return completedMissions; }
    public List<Achievement> getStartedAchievements() { return startedAchievements; }
    public List<Achievement> getCompletedAchievements() { return completedAchievements; }
    public Level getHighestLevel() { return highestLevel; }
    public List<Prize> getPrizes() { return prizes; }

    public void setStartedMissions(List<Mission> startedMissions) { this.startedMissions = startedMissions; }
    public void setCompletedMissions(List<Mission> completedMissions) { this.completedMissions = completedMissions; }
    public void setStartedAchievements(List<Achievement> startedAchievements) { this.startedAchievements = startedAchievements; }
    public void setCompletedAchievements(List<Achievement> completedAchievements) { this.completedAchievements = completedAchievements; }
    public void setHighestLevel(Level highestLevel) { this.highestLevel = highestLevel; }
    public void setPrizes(List<Prize> prizes) { this.prizes = prizes; }
}
