package fr.parisnanterre.iqplay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.parisnanterre.iqplay.dto.LeaderboardResponseDto;
import fr.parisnanterre.iqplay.dto.StartGameRequestDto;
import fr.parisnanterre.iqplay.model.leaderboard.*;
import fr.parisnanterre.iqplay.model.leaderboard.wrapper.AchievementsWrapper;
import fr.parisnanterre.iqplay.model.leaderboard.wrapper.LevelWrapper;
import fr.parisnanterre.iqplay.model.leaderboard.wrapper.MissionsWrapper;
import fr.parisnanterre.iqplay.model.leaderboard.wrapper.PrizeWrapper;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerPlayerService;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/gamelayer")
public class LeaderBoardController {

    private static final Logger log = LoggerFactory.getLogger(GameLayerService.class);
    private final GameLayerPlayerService gameLayerPlayerService;
    private final PlayerService playerService;

    public LeaderBoardController(GameLayerPlayerService gameLayerPlayerService, PlayerService playerService) {
        this.gameLayerPlayerService = gameLayerPlayerService;
        this.playerService = playerService;
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardResponseDto> index() throws Exception {
        IPlayer player = playerService.getCurrentPlayer();
        System.out.println("Fetching missions for player {}"+ player.id());

        HttpResponse<String> missionsResponse = gameLayerPlayerService.getMissionByPlayerId(player.id().toString());
        System.out.println("Missions response: {}"+ missionsResponse.body());

        List<List<Mission>> missions = this.getMissions(missionsResponse);
        List<Mission> startedMissions = missions.get(0);
        List<Mission> completedMissions = missions.get(1);

        HttpResponse<String> achivementsResponse = gameLayerPlayerService.getAchivementsByPlayer(player.id().toString());
        System.out.println("Achievements response: {}"+ achivementsResponse.body());

        ObjectMapper mapper = new ObjectMapper();
        AchievementsWrapper wrapper = mapper.readValue(achivementsResponse.body(), AchievementsWrapper.class);
        List<Achievement> startedAchievements = wrapper.getAchievements().getStarted();
        List<Achievement> completedAchievements = wrapper.getAchievements().getCompleted();

        HttpResponse<String> levelResponse = gameLayerPlayerService.getLevelsByPlayer(player.id().toString());
        System.out.println("Levels response: {}"+ levelResponse.body());

        LevelWrapper levelWrapper = mapper.readValue(levelResponse.body(), LevelWrapper.class);
        Levels levels = levelWrapper.getLevels();
        Level highestLevel = getHighestLevel(levels);

        HttpResponse<String> prizeResponse = gameLayerPlayerService.getPrizesByPlayer(player.id().toString());
        System.out.println("Prizes response: {}"+ prizeResponse.body());

        PrizeWrapper prizeWrapper = mapper.readValue(prizeResponse.body(), PrizeWrapper.class);
        List<Prize> prizes = prizeWrapper.getPrizes();

        LeaderboardResponseDto responseDto = new LeaderboardResponseDto(
                startedMissions, completedMissions,
                startedAchievements, completedAchievements,
                highestLevel, prizes
        );

        log.info("Leaderboard response built successfully");
        return ResponseEntity.ok(responseDto);
    }


    private List<List<Mission>> getMissions(HttpResponse<String> response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MissionsWrapper wrapper = mapper.readValue(response.body(), MissionsWrapper.class);

        List<Mission> startedMissions = wrapper.getMissions().getStarted();
        List<Mission> completedMissions = wrapper.getMissions().getCompleted();

        return List.of(startedMissions, completedMissions);
    }

    private Level getHighestLevel(Levels levels) {
        List<Level> allLevels = new ArrayList<>();
        if (levels.getStarted() != null) {
            allLevels.add(levels.getStarted());
        }
        if (levels.getCompleted() != null && !levels.getCompleted().isEmpty()) {
            allLevels.addAll(levels.getCompleted());
        }
        return allLevels.stream()
                .max(Comparator.comparingInt(Level::getOrdinal))
                .orElse(null);
    }


}
