package fr.parisnanterre.iqplay.mapper;

import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplay.model.Level;
import fr.parisnanterre.iqplay.model.Score;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplay.validator.DefaultIAnswerValidator;
import fr.parisnanterre.iqplaylib.api.*;


public class GameSessionMapper {

    public static GameSessionPersistante toPersistable(GameSession session, Player player) {
        return new GameSessionPersistante(
                player,
                session.name(),
                session.level().level(),
                session.score().score(),
                session.state().toString()
        );
    }

    public static GameSession toDomain(IGame game, GameSessionPersistante persistante, OperationService operationService) {
        // Récupérer le joueur depuis l'entité persistante
        IPlayer player = persistante.getPlayer();

        // Créer l'objet GameSession avec toutes les dépendances
        GameSession session = new GameSession(game, operationService, new DefaultIAnswerValidator(), player);

        // Initialiser la session avec le niveau et le score persistés
        session.start(new Level(persistante.getLevel()), new Score(persistante.getScore()));

        // Restaurer l'état de la session
        session.setState(StateGameSessionEnum.valueOf(persistante.getState()));

        return session;
    }


    public static void updatePersistable(GameSession session, GameSessionPersistante persistante) {
        persistante.setLevel(session.level().level());
        persistante.setScore(session.score().score());
        persistante.setState(session.state().toString());
    }
}

