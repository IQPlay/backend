package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.wikigame.dto.WikiArticleDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.WikiQuestionRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.ReponseRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.*;
import fr.parisnanterre.iqplay.wikigame.repository.FicheRepository;
import fr.parisnanterre.iqplay.wikigame.service.WikiArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fiche")
public class FicheController {

    @Autowired
    private WikiArticleService wikiArticleService;

    @Autowired
    private FicheRepository ficheRepository;

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
        wikiQuestion.setQuestion(getQuestion(wikiQuestionDTO));
        return wikiQuestion;
    }

    private WikiDocument fetchWikiDocumentFromUrl(String url) {
        try {
            ResponseEntity<WikiArticleDTO> response = wikiArticleService.getArticleByUrl(url);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WikiArticleDTO wikiArticleDTO = response.getBody();
                WikiDocument wikiDocument = new WikiDocument();
                wikiDocument.setUrl(url);
                wikiDocument.setWikiId(wikiArticleDTO.getWikiId());
                wikiDocument.setTitle(wikiArticleDTO.getTitle());
                wikiDocument.setContent(wikiArticleDTO.getContent());
                return wikiDocument;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
