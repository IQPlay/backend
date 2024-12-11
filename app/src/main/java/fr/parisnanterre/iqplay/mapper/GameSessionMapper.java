package fr.parisnanterre.iqplay.mapper;

import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.model.GameCalculMental;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.model.Level;
import fr.parisnanterre.iqplay.model.Score;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplaylib.api.IGame;
import fr.parisnanterre.iqplaylib.api.ILevel;
import fr.parisnanterre.iqplaylib.api.IScore;
import fr.parisnanterre.iqplaylib.api.StateGameSessionEnum;

public class GameSessionMapper {

    /**
     * Transforme une instance de GameSession (métier) en une instance persistante.
     *
     * @param session L'objet métier GameSession
     * @param player  Le joueur associé à la session
     * @return Une instance de GameSessionPersistante
     */
    public static GameSessionPersistante toPersistable(GameSession session, Player player) {
        return new GameSessionPersistante(
            player,                               // Associe le joueur
            session.level().level(),              // Récupère le niveau actuel
            session.score().score(),              // Récupère le score actuel
            session.state().toString()            // Convertit l'état en chaîne
        );
    }

    /**
     * Transforme une instance persistante de GameSessionPersistante en une instance métier.
     *
     * @param persistante L'objet GameSessionPersistante
     * @param operationService Le service des opérations
     * @return Une instance de GameSession
     */
    public static GameSession toDomain(IGame game, GameSessionPersistante persistante, OperationService operationService) {
        GameSession session = new GameSession(game, operationService);
        // Reconstruit le niveau et le score
        ILevel level = new Level(persistante.getLevel());
        IScore score = new Score(persistante.getScore());
        // Relancer la partie au niveau enregistre
        session.start(level, score);
        

        return session;
    }
}
