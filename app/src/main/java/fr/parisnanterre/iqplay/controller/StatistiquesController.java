package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.Response;
import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.*;
import fr.parisnanterre.iqplay.dto.StatsDashboardResponseDto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing mental calculation game sessions.
 */
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

    /**
     *
     */
    @PostMapping("/dashboard")
    public ResponseEntity<StatsDashboardResponseDto> getStatisticsForDashboard() {
        try {
            IPlayer player = playerService.getCurrentPlayer();

            StatsDashboardResponseDto dashboardResponseDto = new StatsDashboardResponseDto(1, 1, 1, 1);

            return ResponseEntity.ok(dashboardResponseDto);

        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StatsDashboardResponseDto(1, 1, 1, 1)
            );
        }
    }

}
