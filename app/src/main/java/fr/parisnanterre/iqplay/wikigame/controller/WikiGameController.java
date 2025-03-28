package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.fetch.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.Fiche;
import fr.parisnanterre.iqplay.wikigame.entity.FicheProgress;
import fr.parisnanterre.iqplay.wikigame.repository.FicheProgressRepository;
import fr.parisnanterre.iqplay.wikigame.service.FicheService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WikiGameController {

    @Autowired
    private FicheService ficheService;
    @Autowired
    private PlayerService playerService;

    @PostMapping("/wikigame")
    public ResponseEntity<List<Fiche>> getFichesNonEntamees(@RequestBody FicheRequestDTO ficheRequest) {
        Long playerId = ficheRequest.getPlayerId();
        double lat = ficheRequest.getLat();
        double lon = ficheRequest.getLon();

        List<Fiche> nearbyFiches = ficheService.getFichesNonEntamees(playerId, lat, lon);
        System.out.println("Réponse envoyée au frontend : " + nearbyFiches);
        return ResponseEntity.ok(nearbyFiches);
    }


    @GetMapping("/wikigame/fiches")
    public ResponseEntity<List<Fiche>> getAllPlayerFiches() {

        IPlayer player = playerService.getCurrentPlayer();
        List<Fiche> fiches = ficheService.getFichesEntameesParJoueur(player);

        return ResponseEntity.ok(fiches);
    }


}
