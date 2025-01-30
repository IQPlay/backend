package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.dto.HistoryGameResponseDto;
import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.repository.GameSessionRepository;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.*;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/history")
public class HistoriqueController {

    private final PlayerService playerService;
    private final GameSessionRepository gameSessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(HistoriqueController.class);

    @Autowired
    public HistoriqueController(PlayerService playerService, GameSessionRepository gameSessionRepository) {
        this.playerService = playerService;
        this.gameSessionRepository = gameSessionRepository;
    }

    @GetMapping("/games")
    public ResponseEntity<List<HistoryGameResponseDto>> getHistoryGames() {
        try {
            IPlayer player = playerService.getCurrentPlayer();

            List<GameSessionPersistante> gameSessions = gameSessionRepository.findAllByPlayer(player);
            List<HistoryGameResponseDto> responseDtos = convertToHistoryGameResponseDtos(gameSessions);

            return ResponseEntity.ok(responseDtos);
        } catch (UnauthorizedException ex) {
            logger.warn("Unauthorized access attempt: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        } catch (Exception ex) {
            logger.error("An error occurred while fetching game history", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private List<HistoryGameResponseDto> convertToHistoryGameResponseDtos(List<GameSessionPersistante> gameSessions) {
        return gameSessions.stream()
                .map(session -> new HistoryGameResponseDto(
                        session.getId(),
                        session.getName(),
                        session.getLevel(),
                        session.getScore(),
                        session.getState(),
                        session.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
