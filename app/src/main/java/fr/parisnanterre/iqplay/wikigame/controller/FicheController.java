package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.wikigame.dto.WikiArticleDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.WikiQuestionRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.ReponseRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.*;
import fr.parisnanterre.iqplay.wikigame.repository.FicheRepository;
import fr.parisnanterre.iqplay.wikigame.service.AimlApiService;
import fr.parisnanterre.iqplay.wikigame.service.WikiArticleService;
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
            Fiche fiche = new Fiche();
            fiche.setBadge(ficheRequestDTO.getBadge());
            fiche.setTitre(ficheRequestDTO.getTitre());
            fiche.setDescription(ficheRequestDTO.getDescription());

            List<WikiQuestion> wikiQuestions = new ArrayList<>();
            for (WikiQuestionRequestDTO wikiQuestionDTO : ficheRequestDTO.getWikiQuestions()) {
                WikiQuestion wikiQuestion = getWikiQuestion(wikiQuestionDTO);
                wikiQuestions.add(wikiQuestion);
            }

            fiche.setWikiQuestions(wikiQuestions);
            ficheRepository.save(fiche);
            return new ResponseEntity<>(fiche, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private WikiQuestion getWikiQuestion(WikiQuestionRequestDTO wikiQuestionDTO) {
        WikiQuestion wikiQuestion = new WikiQuestion();
        WikiDocument wikiDocument = fetchWikiDocumentFromUrl(wikiQuestionDTO.getWikiDocument().getUrl());
        wikiQuestion.setWikiDocument(wikiDocument);

        if (wikiQuestionDTO.getQuestion().isGeneratedByAi()) {
            // Si generatedByAi est vrai, l'IA génère la question et les réponses
            try {
                String content = wikiDocument.getContent();
                String qcmJson = aimlApiService.generateQcm(content);
                // Traiter la réponse de l'IA pour en extraire la question et les réponses
                processAiQcmResponse(wikiQuestion, qcmJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            wikiQuestion.setQuestion(getQuestion(wikiQuestionDTO));
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
            content = content.substring(0, 70);  // On garde seulement les 70 premiers caractères
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
