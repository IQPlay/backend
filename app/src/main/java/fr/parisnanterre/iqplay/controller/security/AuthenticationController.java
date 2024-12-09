package fr.parisnanterre.iqplay.controller.security;

import fr.parisnanterre.iqplay.dto.PlayerRequestDto;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.provider.JwtProvider;
import fr.parisnanterre.iqplay.service.JwtBlacklistService;
import fr.parisnanterre.iqplay.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final PlayerService playerService;
    private final JwtProvider jwtProvider;
    private final JwtBlacklistService jwtBlacklistService;

    public AuthenticationController(PlayerService playerService, JwtProvider jwtProvider, JwtBlacklistService jwtBlacklistService) {
        this.playerService = playerService;
        this.jwtProvider = jwtProvider;
        this.jwtBlacklistService = jwtBlacklistService;
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

    @GetMapping("/test")
    public ResponseEntity<?> isAuthenticated() {
        // Récupère l'authentification depuis le SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // L'utilisateur est authentifié
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", authentication.getName()
            ));
        }
        // L'utilisateur n'est pas authentifié
        return ResponseEntity.ok(Map.of(
                "authenticated", false,
                "user", "anonymous"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or missing Authorization header"));
        }

        String token = authorizationHeader.substring(7); // Supprime "Bearer "
        playerService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
    }

}
