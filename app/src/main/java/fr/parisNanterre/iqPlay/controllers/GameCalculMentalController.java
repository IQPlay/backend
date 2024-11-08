package fr.parisNanterre.iqPlay.controllers;

import fr.parisNanterre.iqPlay.models.GameSession;
import fr.parisNanterre.iqPlay.models.Operation;
import fr.parisNanterre.iqPlay.models.Response;
import fr.parisNanterre.iqPlay.services.GameCalculMentalService;
import fr.parisNanterre.iqPlay.services.OperationService;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameCalculMentalController {

    @Autowired
    private GameCalculMentalService gameCalculMentalService;

    @Autowired
    private OperationService operationService;

    // Démarre une nouvelle session avec un niveau de difficulté spécifié et génère des opérations
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam int difficulty) {
        // Créer une nouvelle session avec le niveau de difficulté donné
        GameSession session = gameCalculMentalService.newSession(difficulty);

        if (difficulty == 0) {
            throw new NullPointerException("erreur pointeur");
            // Si la session n'a pas pu être créée, renvoyer une erreur avec un message structuré
//            return ResponseEntity.status(500).body(Map.of(
//                "message", "Erreur lors de la création de la session",
//                "sessionId", null
//            ));
        }
        
        // Générer la séquence d'opérations pour cette session
        List<Operation> operations = operationService.createSequenceNOperation(session.getDifficultyLevel());
        
        if (operations == null || operations.isEmpty()) {
            // Si aucune opération n'a été générée, renvoyer une erreur avec un message structuré
            return ResponseEntity.status(500).body(Map.of(
                "message", "Erreur lors de la génération des opérations",
                "sessionId", null
            ));
        }
    
        session.setCurrentOperations(operations); // Stocker les opérations dans la session
        
        // Retourner un message confirmant que la session a été démarrée et l'ID de session
        return ResponseEntity.ok(Map.of(
            "message", "Session démarrée avec succès.",
            "sessionId", session.getSessionID()
        ));
    }
    
    
    

    // Obtenir la prochaine opération
    @GetMapping("/operation/{sessionId}")
    public ResponseEntity<Operation> getNextOperation(@PathVariable String sessionId) {
        GameSession session = gameCalculMentalService.getSessionById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build(); // Retourne 404 si la session n'existe pas
        }

        // Vérifier s'il y a des opérations disponibles
        List<Operation> operations = session.getCurrentOperations();
        if (operations.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne 204 si aucune opération
        }

        Operation nextOperation = operations.get(0); // Prendre la première opération
        return ResponseEntity.ok(nextOperation);
    }

    // Soumettre une réponse
    @PostMapping("/answer/{sessionId}")
    public ResponseEntity<?> submitAnswer(@PathVariable String sessionId, @RequestParam int userAnswer) {
        GameSession session = gameCalculMentalService.getSessionById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build(); // Retourne 404 si la session n'existe pas
        }

        // Récupérer l'opération actuelle
        List<Operation> operations = session.getCurrentOperations();
        if (operations.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne 204 si aucune opération
        }

        Operation currentOperation = operations.get(0); // Prendre la première opération
        
        // Utiliser le service pour créer la réponse
        Response feedbackResponse = operationService.createResponse(userAnswer, currentOperation);

        // Feedback à l'utilisateur
        String feedback = feedbackResponse.isCorrect() ? "Réponse correcte !" : "Réponse incorrecte ! Le résultat est : " + currentOperation.getResult();

        // Supprimer l'opération de la liste
        operations.remove(0);

        // Préparer la réponse JSON sans renvoyer l'opération suivante
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("feedback", feedback);
        responseBody.put("gameStatus", operations.isEmpty() ? "COMPLETED" : "IN_PROGRESS");

        return ResponseEntity.ok(responseBody);
    }

    // Route pour arrêter et supprimer la session
    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<String> stopGame(@PathVariable String sessionId) {
        // Vérifier si la session existe dans la Map
        GameSession session = gameCalculMentalService.getSessionById(sessionId);

        if (session == null) {
            // Si la session n'existe pas, renvoyer une erreur 404
            return ResponseEntity.notFound().build();
        }

        // Supprimer la session à l'aide du service
        gameCalculMentalService.removeSession(sessionId);

        // Retourner une réponse de succès
        return ResponseEntity.ok("Session terminée et supprimée en mémoire. Le jeu est arrêté.");
    }

    // Ajuster la difficulté et continuer le jeu
    @PostMapping("/continue/{sessionId}")
    public ResponseEntity<String> continueGame(@PathVariable String sessionId) {
        // Récupérer la session avec l'ID donné
        GameSession session = gameCalculMentalService.getSessionById(sessionId);
    
        // Si la session n'existe pas, retourner une erreur 404
        if (session == null) {
            return ResponseEntity.notFound().build(); 
        }
    
        // Mettre à jour la difficulté de la session en fonction du taux de succès actuel
        gameCalculMentalService.updateDifficultyLevel(session.getSuccessTargetRate(), session);
    
        // Générer une nouvelle séquence d'opérations pour cette session
        List<Operation> newOperations = operationService.createSequenceNOperation(session.getDifficultyLevel());
        session.setCurrentOperations(newOperations); // Mettre à jour la liste des opérations
    
        // Retourner une réponse de succès avec un message
        return ResponseEntity.ok("Difficulté mise à jour. Nouvelles opérations générées.");
    }
    
}
