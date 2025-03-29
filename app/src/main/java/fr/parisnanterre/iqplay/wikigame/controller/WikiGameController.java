package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.fetch.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.Fiche;
import fr.parisnanterre.iqplay.wikigame.entity.FicheProgress;
import fr.parisnanterre.iqplay.wikigame.entity.Reponse;
import fr.parisnanterre.iqplay.wikigame.entity.WikiQuestion;
import fr.parisnanterre.iqplay.wikigame.repository.FicheProgressRepository;
import fr.parisnanterre.iqplay.wikigame.service.FicheService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
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


    /**
     * Récupérer toutes les fiches entamées par le joueur
     * @return
     */
    @GetMapping("/wikigame/fiches")
    public ResponseEntity<List<Fiche>> getAllPlayerFiches() {

        IPlayer player = playerService.getCurrentPlayer();
        List<Fiche> fiches = ficheService.getFichesEntameesParJoueur(player);

        return ResponseEntity.ok(fiches);
    }

    /**
     * Retourne une fiche par son ID
     * @param ficheId
     * @return
     */
    @GetMapping("/wikigame/fiches/{ficheId}")
    public ResponseEntity<Fiche> getFicheById(@PathVariable Long ficheId) {
        Fiche fiche = ficheService.getFicheById(ficheId);
        if (fiche != null) {
            return ResponseEntity.ok(fiche);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Créer une fiche progress pour le joueur
     * @return
     */
    @PostMapping("/wikigame/fiches/{ficheId}/progress")
    public ResponseEntity<FicheProgress> createFicheProgress(@PathVariable Long ficheId) {
        Fiche fiche = ficheService.getFicheById(ficheId);
        IPlayer player = playerService.getCurrentPlayer();

        FicheProgress createdFicheProgress = ficheService.createFicheProgress(fiche, (Player) player);
        return ResponseEntity.ok(createdFicheProgress);
    }

    /**
     * Retourne les questions d'une fiche en progression du joueur
     * @param ficheId
     * @return
     */
    @GetMapping("/wikigame/fiches/{ficheId}/questions")
    public ResponseEntity<List<WikiQuestion>> getWikiQuestions(@PathVariable Long ficheId) {
        FicheProgress ficheProgress = ficheService.getFicheProgressForPlayer(ficheId, playerService.getCurrentPlayer().id());

        if (ficheProgress == null) {
            return ResponseEntity.notFound().build();
        }

        List<WikiQuestion> wikiQuestions = ficheProgress.getFiche().getWikiQuestions();

        return ResponseEntity.ok(wikiQuestions);
    }

    /**
     * Retourne les réponses possibles pour une question donnée
     * @param ficheId
     * @param questionId
     * @return
     */
    @GetMapping("/wikigame/fiches/{ficheId}/questions/{questionId}/answers")
    public ResponseEntity<List<String>> getAnswersForQuestion(@PathVariable Long ficheId, @PathVariable Long questionId) {
        Fiche fiche = ficheService.getFicheById(ficheId);
        if (fiche == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<WikiQuestion> questionOptional = fiche.getWikiQuestions().stream()
                .filter(wq -> wq.getId().equals(questionId))
                .findFirst();

        if (questionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        WikiQuestion question = questionOptional.get();

        return ResponseEntity.ok(question.getQuestion().getReponses().stream()
                .map(Reponse::getReponse)
                .collect(Collectors.toList()));
    }

//    @PostMapping("/wikigame/fiches/{ficheId}/questions/{questionId}/answer")
//    public ResponseEntity<String> answerQuestion(@PathVariable Long ficheId,
//                                                 @PathVariable Long questionId,
//                                                 @RequestParam boolean isCorrect) {
//        FicheProgress ficheProgress = ficheProgressRepository.findByFicheIdAndPlayerId(ficheId, playerService.getCurrentPlayer().getId());
//
//        if (ficheProgress == null) {
//            return ResponseEntity.notFound().build(); // Si la fiche n'est pas trouvée
//        }
//
//        ficheService.updateQuestionProgress(ficheProgress, questionId, isCorrect);
//        return ResponseEntity.ok("Réponse enregistrée");
//    }

}
