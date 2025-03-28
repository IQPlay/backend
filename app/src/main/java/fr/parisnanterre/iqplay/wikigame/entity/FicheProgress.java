package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.entity.Player;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FicheProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Fiche fiche;

    private boolean estTerminee;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

}
