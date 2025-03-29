package fr.parisnanterre.iqplay.wikigame.repository;

import fr.parisnanterre.iqplay.wikigame.entity.FicheProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FicheProgressRepository extends JpaRepository<FicheProgress, Long> {
    List<FicheProgress> findByPlayerId(Long playerId);

    FicheProgress findByFicheIdAndPlayerId(Long ficheId, Long playerId);
}
