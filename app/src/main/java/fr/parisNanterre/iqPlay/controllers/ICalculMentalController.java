package fr.parisNanterre.iqPlay.controllers;

import org.springframework.web.bind.annotation.RequestParam;

public interface ICalculMentalController {
    String getCalcul();
    boolean verifierResultat(@RequestParam("resultat") int resultat);
}
