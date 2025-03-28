package fr.parisnanterre.iqplay.wikigame.repository;

import fr.parisnanterre.iqplay.wikigame.entity.Fiche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FicheRepository extends JpaRepository<Fiche, Long> {

    @Query("SELECT f FROM Fiche f WHERE f.id NOT IN (SELECT fp.fiche.id FROM FicheProgress fp WHERE fp.player.id = :playerId)")
    List<Fiche> findFichesNonEntameesByPlayerId(@Param("playerId") Long playerId);


}
