package fr.parisnanterre.iqplay.repository;

import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplaylib.api.IPlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionPersistante, Long> {

    // Trouver des sessions par date exacte
    List<GameSessionPersistante> findByCreatedAt(LocalDateTime createdAt);

    // Trouver des sessions créées après une certaine date
    List<GameSessionPersistante> findByCreatedAtAfter(LocalDateTime createdAt);

    // Trouver des sessions créées dans une plage de dates
    List<GameSessionPersistante> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Trouver des sessions associées à un joueur
    List<GameSessionPersistante> findByPlayer(IPlayer player);

    // Trouver des sessions associées à un joueur et un état spécifique
    List<GameSessionPersistante> findByPlayerAndState(IPlayer player, String state);
}
