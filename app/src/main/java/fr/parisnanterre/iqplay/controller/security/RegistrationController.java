package fr.parisnanterre.iqplay.controller.security;

import fr.parisnanterre.iqplay.dto.PlayerRequestDto;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final PlayerService playerService;

    @Autowired
    public RegistrationController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerRequestDto request) {
        try {
            if (request.email() == null || request.username() == null || request.password() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email, username, and password cannot be null or empty."));
            }

            Player player = playerService.registerPlayer(request.email(), request.username(), request.password());

            return ResponseEntity.ok(Map.of(
                    "message", "Player registered successfully",
                    "playerId", player.id()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
