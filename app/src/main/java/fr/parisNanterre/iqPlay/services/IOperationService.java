package fr.parisNanterre.iqPlay.services;

import fr.parisNanterre.iqPlay.models.Operation;
import fr.parisNanterre.iqPlay.models.Response;

import java.util.List;

public interface IOperationService {

    // Méthode pour obtenir une liste d'opérateurs
    List<Character> obtainsOperand(int nbOperateur);

    // Méthode pour créer une opération
    Operation createOperation(int difficulty);

    // Méthode pour générer un nombre aléatoire avec un nombre spécifique de chiffres
    int generateRandomNumberWithDigits(int numDigits);

    List<Operation> createSequenceNOperation(int difficulty);

    // Méthode pour savoir si une réponse est correcte
    boolean isCorrectResponse(Response response);

    /**
     * Méthode pour calculer le ratio de bonnes réponses
     * @param responses Liste des réponses fournies par l'utilisateur
     * @param operations Liste des opérations à comparer
     * @return Ratio de bonnes réponses (entre 0 et 1)
     */

     // Méthode pour calculer le % de bonne réponse
    double calculateCorrectAnswerRatio(List<Response> responses);
}
