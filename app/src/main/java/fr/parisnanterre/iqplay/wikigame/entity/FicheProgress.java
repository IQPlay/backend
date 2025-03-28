package fr.parisnanterre.iqplay.wikigame.entity;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.wikigame.entity.api.IFicheProgress;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FicheProgress implements IFicheProgress {

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

    @Override
    public Fiche getFiche() {
        return fiche;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isEstTerminee() {
        return estTerminee;
    }

    @Override
    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
    }

    @Override
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    @Override
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public LocalDateTime getDateFin() {
        return dateFin;
    }

    @Override
    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }

    @Override
    public String toString() {
        return "FicheProgress{" +
                "id=" + id +
                ", player=" + player +
                ", fiche=" + fiche +
                ", estTerminee=" + estTerminee +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
