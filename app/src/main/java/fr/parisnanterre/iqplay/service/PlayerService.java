package fr.parisnanterre.iqplay.service;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        return playerRepository.save(player);
    }

}
