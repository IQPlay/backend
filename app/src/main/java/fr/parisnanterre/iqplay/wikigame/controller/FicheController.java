package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplay.wikigame.dto.WikiArticleDTO;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.create.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.create.WikiQuestionRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.fiche.create.ReponseRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.*;
import fr.parisnanterre.iqplay.wikigame.repository.FicheRepository;
import fr.parisnanterre.iqplay.wikigame.service.AimlApiService;
import fr.parisnanterre.iqplay.wikigame.service.WikiArticleService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerEventService;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerPlayerService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fiche")
public class FicheController {

    @Autowired
    private WikiArticleService wikiArticleService;

    @Autowired
    private FicheRepository ficheRepository;

    @Autowired
    private AimlApiService aimlApiService;

    private final GameLayerEventService gameLayerEventService;
    private final GameLayerPlayerService gameLayerPlayerService;
    private final PlayerService playerService;

    public FicheController(GameLayerEventService gameLayerEventService, GameLayerPlayerService gameLayerPlayerService, PlayerService playerService) {
        this.gameLayerEventService = gameLayerEventService;
        this.gameLayerPlayerService = gameLayerPlayerService;
        this.playerService = playerService;
    }

    /**
     * Étape 1 : Charger les données d'un WikiDocument à partir de son URL
     */
    @GetMapping("/load-wiki")
    public ResponseEntity<WikiDocument> loadWikiDocument(@RequestParam String url) {
        try {
            ResponseEntity<WikiArticleDTO> response = wikiArticleService.getArticleByUrl(url);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WikiArticleDTO wikiArticleDTO = response.getBody();
                WikiDocument wikiDocument = new WikiDocument();
                wikiDocument.setUrl(url);
                wikiDocument.setWikiId(wikiArticleDTO.getWikiId());
                wikiDocument.setTitle(wikiArticleDTO.getTitle());
                wikiDocument.setContent(wikiArticleDTO.getContent());
                return ResponseEntity.ok(wikiDocument);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Étape 2 : Créer une fiche en utilisant uniquement l'URL du WikiDocument
     */
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<Fiche> create(@RequestBody FicheRequestDTO ficheRequestDTO) {
        try {
            IPlayer player = playerService.getCurrentPlayer();
            Fiche fiche = new Fiche();
            fiche.setBadge(ficheRequestDTO.getBadge());
            fiche.setTitre(ficheRequestDTO.getTitre());
            fiche.setDescription(ficheRequestDTO.getDescription());

            List<WikiQuestion> wikiQuestions = new ArrayList<>();

            for (WikiQuestionRequestDTO wikiQuestionDTO : ficheRequestDTO.getWikiQuestions()) {
                WikiQuestion wikiQuestion = getWikiQuestion(wikiQuestionDTO, player);
                wikiQuestions.add(wikiQuestion);
            }

            for (WikiQuestion wikiQuestion : wikiQuestions) {
                wikiQuestion.setFiche(fiche);
            }

            fiche.setWikiQuestions(wikiQuestions);

            ficheRepository.save(fiche);
            return new ResponseEntity<>(fiche, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private WikiQuestion getWikiQuestion(WikiQuestionRequestDTO wikiQuestionDTO, IPlayer player) throws InterruptedException, IOException {
        WikiQuestion wikiQuestion = new WikiQuestion();

        // Récupérer le WikiDocument via l'URL fournie
        WikiDocument wikiDocument = fetchWikiDocumentFromUrl(wikiQuestionDTO.getWikiDocument().getUrl());
        if (wikiDocument != null) {
            wikiQuestion.setWikiDocument(wikiDocument);
        } else {
            throw new IllegalArgumentException("WikiDocument introuvable pour l'URL fournie.");
        }

        // Si la question est générée par l'IA, traiter la génération des réponses
        if (wikiQuestionDTO.getQuestion().isGeneratedByAi()) {
            try {
                String content = wikiDocument.getContent();
                String qcmJson = aimlApiService.generateQcm(content);
                processAiQcmResponse(wikiQuestion, qcmJson);
                this.gameLayerEventService.completeEvent("create-fiche-with-ia", player.id().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            wikiQuestion.setQuestion(getQuestion(wikiQuestionDTO));  // Sinon, on récupère la question fournie par l'utilisateur
            this.gameLayerEventService.completeEvent("create-fiche", player.id().toString());
        }

        return wikiQuestion;
    }

    private void processAiQcmResponse(WikiQuestion wikiQuestion, String qcmJson) {
        try {
            JSONObject jsonResponse = new JSONObject(qcmJson);
            JSONArray qcmArray = jsonResponse.getJSONArray("qcm");
            JSONObject qcm = qcmArray.getJSONObject(0);
            String questionText = qcm.getString("question");
            wikiQuestion.setQuestion(new Question(questionText, true));

            // Initialisation de la liste des réponses si elle est null. Important car sinon on a un NullPointerException
            if (wikiQuestion.getQuestion().getReponses() == null) {
                wikiQuestion.getQuestion().setReponses(new ArrayList<>());
            }

            // Ajout des réponses à la question générée par l'IA
            JSONArray responses = qcm.getJSONArray("reponses");
            for (int i = 0; i < responses.length(); i++) {
                JSONObject responseJson = responses.getJSONObject(i);
                Reponse response = new Reponse();
                response.setReponse(responseJson.getString("reponse"));
                response.setCorrect(responseJson.getBoolean("correct"));
                response.setQuestion(wikiQuestion.getQuestion());
                wikiQuestion.getQuestion().getReponses().add(response);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private WikiDocument fetchWikiDocumentFromUrl(String url) {
        try {
            ResponseEntity<WikiArticleDTO> response = wikiArticleService.getArticleByUrl(url);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WikiDocument wikiDocument = getWikiDocument(url, response);
                return wikiDocument;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private static WikiDocument getWikiDocument(String url, ResponseEntity<WikiArticleDTO> response) {
        WikiArticleDTO wikiArticleDTO = response.getBody();
        WikiDocument wikiDocument = new WikiDocument();
        wikiDocument.setUrl(url);
        wikiDocument.setWikiId(wikiArticleDTO.getWikiId());
        wikiDocument.setTitle(wikiArticleDTO.getTitle());

        String content = wikiArticleDTO.getContent();
        if (content != null && content.length() > 70) {
            content = content.substring(0, 70);  // On garde seulement les 70 premiers caractères pour eviter les erreurs de limite mysql
        }
        wikiDocument.setContent(content);
        return wikiDocument;
    }

    private static Question getQuestion(WikiQuestionRequestDTO wikiQuestionDTO) {
        Question question = new Question();
        question.setIntitule(wikiQuestionDTO.getQuestion().getIntitule());
        question.setGeneratedByAi(wikiQuestionDTO.getQuestion().isGeneratedByAi());

        List<Reponse> reponses = new ArrayList<>();
        for (ReponseRequestDTO reponseDTO : wikiQuestionDTO.getQuestion().getReponses()) {
            Reponse reponse = new Reponse();
            reponse.setReponse(reponseDTO.getReponse());
            reponse.setCorrect(reponseDTO.isCorrect());
            reponse.setQuestion(question);
            reponses.add(reponse);
        }
        question.setReponses(reponses);
        return question;
    }


}
