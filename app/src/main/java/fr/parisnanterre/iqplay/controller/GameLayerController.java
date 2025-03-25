package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplay.service.gamelayer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gamelayer")
public class GameLayerController {

    private final PlayerService playerService;
    private final GameLayerPlayerIntegrationService playerIntegrationService;
    private final GameLayerAchievementIntegrationService achievementService;
    private final GameLayerEventIntegrationService eventService;
    private final GameLayerLeaderBoardIntegrationService leaderboardService;
    private final GameLayerLevelIntegrationService levelService;
    private final GameLayerMissionIntegrationService missionService;
    private final GameLayerStreakIntegrationService streakService;

    @Autowired
    public GameLayerController(PlayerService playerService,
                               GameLayerPlayerIntegrationService playerIntegrationService,
                               GameLayerAchievementIntegrationService achievementService,
                               GameLayerEventIntegrationService eventService,
                               GameLayerLeaderBoardIntegrationService leaderboardService,
                               GameLayerLevelIntegrationService levelService,
                               GameLayerMissionIntegrationService missionService,
                               GameLayerStreakIntegrationService streakService) {
        this.playerService = playerService;
        this.playerIntegrationService = playerIntegrationService;
        this.achievementService = achievementService;
        this.eventService = eventService;
        this.leaderboardService = leaderboardService;
        this.levelService = levelService;
        this.missionService = missionService;
        this.streakService = streakService;
    }

    private String getCurrentUsername() {
        return ((Player) playerService.getCurrentPlayer()).username();
    }

    @GetMapping("/player/data")
    public ResponseEntity<?> getPlayerInfo() {
        return ResponseEntity.ok(playerIntegrationService.getPlayerInfo(getCurrentUsername()));
    }

    @GetMapping("/player/achievements")
    public ResponseEntity<?> getPlayerAchievements() {
        return ResponseEntity.ok(playerIntegrationService.getAchievements(getCurrentUsername()));
    }

    @GetMapping("/player/events")
    public ResponseEntity<?> getPlayerEvents() {
        return ResponseEntity.ok(playerIntegrationService.getEvents(getCurrentUsername()));
    }

    @GetMapping("/player/levels")
    public ResponseEntity<?> getPlayerLevels() {
        return ResponseEntity.ok(playerIntegrationService.getLevels(getCurrentUsername()));
    }

    @GetMapping("/player/missions")
    public ResponseEntity<?> getPlayerMissions() {
        return ResponseEntity.ok(playerIntegrationService.getMissions(getCurrentUsername()));
    }

    @GetMapping("/player/missions/{id}")
    public ResponseEntity<?> getPlayerMissionById(@PathVariable String id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }


    @GetMapping("/player/ranking")
    public ResponseEntity<?> getPlayerRanking() {
        return ResponseEntity.ok(playerIntegrationService.getRanking(getCurrentUsername()));
    }

    @GetMapping("/player/prizes")
    public ResponseEntity<?> getPlayerPrizes() {
        return ResponseEntity.ok(playerIntegrationService.getPrizes(getCurrentUsername()));
    }

    @GetMapping("/player/streak")
    public ResponseEntity<?> getPlayerStreak() {
        return ResponseEntity.ok(playerIntegrationService.getStreak(getCurrentUsername()));
    }

    @GetMapping("/achievements/{id}")
    public ResponseEntity<?> getAchievementById(@PathVariable String id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping("/events/{id}/complete")
    public ResponseEntity<?> completeEvent(@PathVariable String id) {
        return ResponseEntity.ok(eventService.completeEvent(id, getCurrentUsername()));
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<?> getAllLeaderboards() {
        return ResponseEntity.ok(leaderboardService.getAllLeaderboards());
    }

    @GetMapping("/leaderboards/{id}")
    public ResponseEntity<?> getLeaderboardById(@PathVariable String id) {
        return ResponseEntity.ok(leaderboardService.getLeaderboardById(id));
    }

    @GetMapping("/levels/{id}")
    public ResponseEntity<?> getLevelById(@PathVariable String id) {
        return ResponseEntity.ok(levelService.getLevelById(id));
    }

    @GetMapping("/missions")
    public ResponseEntity<?> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/missions/{id}")
    public ResponseEntity<?> getMissionById(@PathVariable String id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @GetMapping("/streaks")
    public ResponseEntity<?> getAllStreaks() {
        return ResponseEntity.ok(streakService.getAllStreaks());
    }

    @GetMapping("/streaks/{id}")
    public ResponseEntity<?> getStreakById(@PathVariable String id) {
        return ResponseEntity.ok(streakService.getStreakById(id));
    }
}
