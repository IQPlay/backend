package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.fetch.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.*;
import fr.parisnanterre.iqplay.wikigame.repository.FicheProgressRepository;
import fr.parisnanterre.iqplay.wikigame.repository.QuestionRepository;
import fr.parisnanterre.iqplay.wikigame.service.FicheService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private final GameLayerEventService gameLayerEventService;

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final FicheProgressRepository ficheProgressRepository;

    public WikiGameController(GameLayerEventService gameLayerEventService, QuestionRepository questionRepository, FicheProgressRepository ficheProgressRepository) {
        this.gameLayerEventService = gameLayerEventService;
        this.questionRepository = questionRepository;
        this.ficheProgressRepository = ficheProgressRepository;
    }

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

    /**
     * Enregistre la réponse du joueur à une question si elle est correcte
     */
    @PostMapping("/wikigame/fiches/{ficheId}/questions/{questionId}/answer/{answerId}")
    public ResponseEntity<String> answerQuestion(@PathVariable Long ficheId,
                                                 @PathVariable Long questionId,
                                                 @PathVariable Long answerId) throws IOException, InterruptedException {

        FicheProgress ficheProgress = ficheService.getFicheProgressForPlayer(ficheId, playerService.getCurrentPlayer().id());
        if (ficheProgress == null) {
            return ResponseEntity.notFound().build();
        }

        boolean dejaRepondu = ficheProgress.getQuestionProgressList().stream()
                .anyMatch(qp -> qp.getWikiQuestion().getId().equals(questionId));
        if (dejaRepondu) {
            return ResponseEntity.badRequest().body("Vous avez déjà répondu à cette question.");
        }

        Question question = questionRepository.getReferenceById(questionId);

        Optional<Reponse> optReponse = question.getReponses().stream()
                .filter(r -> r.getId().equals(answerId))
                .findFirst();

        if (optReponse.isEmpty()) {
            return ResponseEntity.badRequest().body("Réponse non trouvée");
        }

        Reponse selectedAnswer = optReponse.get();
        boolean isCorrect = selectedAnswer.isCorrect();

        if (isCorrect) {
            ficheService.updateQuestionProgress(ficheProgress, questionId, isCorrect);

            int nbQuestionsRepondues = ficheProgress.getQuestionProgressList().size();
            int nbTotalQuestions = ficheProgress.getFiche().getWikiQuestions().size();

            if (nbQuestionsRepondues == nbTotalQuestions) {

                ficheProgress.setEstTerminee(true);
                ficheProgress.setDateFin(LocalDateTime.now());
                ficheProgressRepository.save(ficheProgress);
                gameLayerEventService.completeEvent("wiki-win-fiche", ficheProgress.getPlayer().id().toString());
            }

            gameLayerEventService.completeEvent("wiki-win-question", ficheProgress.getPlayer().id().toString());
            return ResponseEntity.ok("Bonne réponse");
        } else {
            gameLayerEventService.completeEvent("wiki-lose-question", ficheProgress.getPlayer().id().toString());
            return ResponseEntity.ok("Mauvaise réponse");
        }
    }



}
