package fr.parisnanterre.iqplay.wikigame.repository;

import fr.parisnanterre.iqplay.wikigame.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
