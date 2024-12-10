package fr.parisnanterre.iqplay.controller.security;

import fr.parisnanterre.iqplay.dto.PlayerRequestDto;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.provider.JwtProvider;
import fr.parisnanterre.iqplay.service.JwtBlacklistService;
import fr.parisnanterre.iqplay.service.PlayerService;
import org.springframework.http.ResponseCookie;
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

            // Création d'un cookie sécurisé
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)  // Rend le cookie inaccessible via JavaScript
                    .secure(true)    // En locale, je désactive le HTTPS
                    .path("/")       // Rend le cookie disponible sur tout le site
                    .sameSite("None") // pour autoriser les cookies cross-site en dev. Strict Mettre en Prévention contre les attaques CSRF
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString()) // Ajout du cookie dans l'en-tête de réponse
                    .body(Map.of("message", "Login successful"));
        } catch (IllegalArgumentException e) {
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
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)  // Rend le cookie inaccessible via JavaScript
                .secure(true)    // En locale, je désactive le HTTPS
                .path("/")       // Rend le cookie disponible sur tout le site
                .sameSite("None") // pour autoriser les cookies cross-site en dev. Strict Mettre en Prévention contre les attaques CSRF
                .maxAge(0) // Expire immédiatement
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(Map.of("message", "Successfully logged out"));
    }

}
