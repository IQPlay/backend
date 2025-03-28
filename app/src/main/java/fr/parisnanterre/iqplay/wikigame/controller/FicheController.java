package fr.parisnanterre.iqplay.wikigame.controller;

import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.FicheRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.WikiQuestionRequestDTO;
import fr.parisnanterre.iqplay.wikigame.dto.create.fiche.ReponseRequestDTO;
import fr.parisnanterre.iqplay.wikigame.entity.*;
import fr.parisnanterre.iqplay.wikigame.repository.FicheRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fiche")
public class FicheController {

    private final FicheRepository ficheRepository;

    public FicheController(FicheRepository ficheRepository) {
        this.ficheRepository = ficheRepository;
    }

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

    @NotNull
    private static WikiQuestion getWikiQuestion(WikiQuestionRequestDTO wikiQuestionDTO) {
        WikiQuestion wikiQuestion = new WikiQuestion();

        WikiDocument wikiDocument = new WikiDocument();
        wikiDocument.setUrl(wikiQuestionDTO.getWikiDocument().getUrl());
        wikiDocument.setContent(wikiQuestionDTO.getWikiDocument().getContent());
        wikiDocument.setTitle(wikiQuestionDTO.getWikiDocument().getTitle());

        Question question = getQuestion(wikiQuestionDTO);

        wikiQuestion.setWikiDocument(wikiDocument);
        wikiQuestion.setQuestion(question);
        return wikiQuestion;
    }

    @NotNull
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
