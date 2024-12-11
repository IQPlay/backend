package fr.parisnanterre.iqplay.repository;

import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
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

    // Trouver des sessions dans une plage de dates
    List<GameSessionPersistante> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Trouver des sessions associées à un jeu par son nom
    List<GameSessionPersistante> findByGameName(String gameName);
}
