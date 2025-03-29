package fr.parisnanterre.iqplay.wikigame.repository;

import fr.parisnanterre.iqplay.wikigame.entity.QuestionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionProgressRepository extends JpaRepository<QuestionProgress, Long> {
}
