package fr.parisnanterre.iqplay.controllers.security;

import fr.parisnanterre.iqplay.dto.PlayerRegistrationRequest;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final PlayerService playerService;

    @Autowired
    public RegistrationController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerRegistrationRequest request) {
        try {
            // Debug log for incoming request
            System.out.println("Email: " + request.email());
            System.out.println("Username: " + request.username());
            System.out.println("Password: " + request.password());

            if (request.email() == null || request.username() == null || request.password() == null) {
                return ResponseEntity.badRequest().body("Email, username, and password cannot be null or empty.");
            }

            Player player = playerService.registerPlayer(request.email(), request.username(), request.password());
            return ResponseEntity.ok("Player registered successfully with ID: " + player.id());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
