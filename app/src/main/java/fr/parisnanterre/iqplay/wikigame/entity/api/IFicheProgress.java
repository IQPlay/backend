package fr.parisnanterre.iqplay.wikigame.entity.api;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.wikigame.entity.Fiche;

import java.time.LocalDateTime;

public interface IFicheProgress {
     Fiche getFiche() ;
     Player getPlayer();
     void setPlayer(Player player);
     boolean isEstTerminee();
     void setEstTerminee(boolean estTerminee);
     LocalDateTime getDateDebut();
     void setDateDebut(LocalDateTime dateDebut);
     LocalDateTime getDateFin();
     void setDateFin(LocalDateTime dateFin);
     void setFiche(Fiche fiche);
}
