package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplay.dto.StatsDashboardResponseDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/stats")
public class StatistiquesController {

    private final PlayerService playerService;
    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public StatistiquesController(PlayerService playerService, GameSessionRepository gameSessionRepository) {
        this.playerService = playerService;
        this.gameSessionRepository = gameSessionRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StatsDashboardResponseDto> getStatisticsForDashboard() {
        try {
            IPlayer player = playerService.getCurrentPlayer();

            List<GameSessionPersistante> gameSessionPersistantes = this.gameSessionRepository.findAllByPlayer(player);

            int nbGames = gameSessionPersistantes.size();
            int avgScore = gameSessionPersistantes.stream().mapToInt(GameSessionPersistante::getScore).sum() / nbGames;
            int bestScore = gameSessionPersistantes.stream().mapToInt(GameSessionPersistante::getScore).max().orElse(0);
            int serieDay = 0;

            StatsDashboardResponseDto dashboardResponseDto = new StatsDashboardResponseDto(nbGames, avgScore, bestScore, serieDay);

            return ResponseEntity.ok(dashboardResponseDto);

        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
