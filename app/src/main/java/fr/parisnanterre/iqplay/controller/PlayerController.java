package fr.parisnanterre.iqplay.controller;
import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Route to get the player's profile data.
     *
     * @return ResponseEntity containing player profile data.
     */
    @GetMapping("/profile")
    public ResponseEntity<PlayerProfileDto> getProfile() {
        IPlayer player = playerService.getCurrentPlayer();
        PlayerProfileDto profile = new PlayerProfileDto(
                player.id(),
                ((Player) player).email(),
                ((Player) player).username()
        );
        return ResponseEntity.ok(profile);
    }

}
