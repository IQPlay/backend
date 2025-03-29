package fr.parisnanterre.iqplay.wikigame.repository;

import fr.parisnanterre.iqplay.wikigame.entity.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
}
