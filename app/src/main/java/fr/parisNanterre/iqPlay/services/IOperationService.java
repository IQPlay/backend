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

    List<Operation> createSequence3Operation(int difficulty);

    // Méthode pour comparer une réponse avec le résultat d'une opération
    boolean isCorrectResponse(Response response, Operation operation);

    /**
     * Méthode pour calculer le ratio de bonnes réponses
     * @param responses Liste des réponses fournies par l'utilisateur
     * @param operations Liste des opérations à comparer
     * @return Ratio de bonnes réponses (entre 0 et 1)
     */
    double calculateCorrectAnswerRatio(List<Response> responses, List<Operation> operations);
}
