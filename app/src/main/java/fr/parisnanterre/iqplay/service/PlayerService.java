package fr.parisnanterre.iqplay.service;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.exception.UnauthorizedException;
import fr.parisnanterre.iqplay.repository.PlayerRepository;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository,
                         PasswordEncoder passwordEncoder,
                         JwtBlacklistService jwtBlacklistService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    /**
     * Enregistre un nouveau joueur dans la base de données et sur GameLayer.
     */
    public Player registerPlayer(String email, String username, String password) {
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException("Email, username, and password cannot be null.");
        }

        if (playerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (playerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use.");
        }

        Player player = new Player();
        player.email(email);
        player.username(username);
        player.password(passwordEncoder.encode(password));

        Player savedPlayer = playerRepository.save(player);

        return savedPlayer;
    }

    /**
     * Supprime un joueur localement et sur GameLayer.
     */
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No player found with this ID."));

        playerRepository.delete(player);
    }

    /**
     * Authentifie un joueur en vérifiant son email et son mot de passe.
     */
    public Player authenticatePlayer(String email, String password) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No player found with this email."));

        if (!passwordEncoder.matches(password, player.password())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return player;
    }

    /**
     * Récupère le joueur actuellement authentifié.
     */
    public IPlayer getCurrentPlayer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("No authenticated player found.");
        }

        String username = authentication.getName();
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Authenticated player not found in database."));
    }

    /**
     * Invalide un token JWT et déconnecte le joueur.
     */
    public void logout(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty.");
        }
        jwtBlacklistService.invalidateToken(token);
        SecurityContextHolder.clearContext();
    }
}
