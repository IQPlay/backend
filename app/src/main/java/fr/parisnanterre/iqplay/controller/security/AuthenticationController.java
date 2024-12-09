package fr.parisnanterre.iqplay.controller.security;

import fr.parisnanterre.iqplay.dto.PlayerRequestDto;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.provider.JwtProvider;
import fr.parisnanterre.iqplay.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final PlayerService playerService;
    private final JwtProvider jwtProvider;

    public AuthenticationController(PlayerService playerService, JwtProvider jwtProvider) {
        this.playerService = playerService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PlayerRequestDto request) {
        if (request.email() == null || request.password() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email and password must be provided."
            ));
        }

        try {
            Player player = playerService.authenticatePlayer(request.email(), request.password());

            // Génération du JWT
            String token = jwtProvider.generateToken(
                    new UsernamePasswordAuthenticationToken(player.email(), null)
            );

            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs (email non trouvé ou mot de passe invalide)
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
