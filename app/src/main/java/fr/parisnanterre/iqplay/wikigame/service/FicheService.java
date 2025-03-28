package fr.parisnanterre.iqplay.wikigame.service;

import fr.parisnanterre.iqplay.wikigame.GeoCoordinates;
import fr.parisnanterre.iqplay.wikigame.entity.Fiche;
import fr.parisnanterre.iqplay.wikigame.entity.WikiDocument;
import fr.parisnanterre.iqplay.wikigame.repository.FicheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FicheService {
    @Autowired
    private FicheRepository ficheRepository;

    @Autowired
    private WikiGeoService wikiGeoService;

    public List<Fiche> getFichesNonEntamees(Long playerId, double lat, double lon) {

        List<Fiche> fichesNonEntamees = ficheRepository.findFichesNonEntameesByPlayerId(playerId);
        System.out.println("Fiches non entamées trouvées : " + fichesNonEntamees);

        List<Fiche> nearbyFiches = new ArrayList<>();

        for (Fiche fiche : fichesNonEntamees) {
            System.out.println("Traitement de la fiche : " + fiche.getTitre());

            // Vérification si la fiche contient des questions
            if (!fiche.getWikiQuestions().isEmpty()) {
                WikiDocument firstDocument = fiche.getWikiQuestions().getFirst().getWikiDocument();
                System.out.println("Premier WikiDocument trouvé : " + firstDocument);

                String wikiId = firstDocument.getWikiId();
                System.out.println("WikiId du premier document : " + wikiId);

                GeoCoordinates geoCoordinates = wikiGeoService.getGeoCoordinates(wikiId);
                System.out.println("Coordonnées récupérées pour " + wikiId + " : " + geoCoordinates);

                if (geoCoordinates != null) {
                    double distance = calculateDistance(lat, lon, geoCoordinates.getLatitude(), geoCoordinates.getLongitude());
                    System.out.println("Distance entre (" + lat + "," + lon + ") et ("
                            + geoCoordinates.getLatitude() + "," + geoCoordinates.getLongitude() + ") = " + distance + " km");

                    if (distance <= 2) {
                        nearbyFiches.add(fiche);
                        System.out.println("Fiche ajoutée aux résultats : " + fiche.getTitre());
                    } else {
                        System.out.println("Fiche exclue car distance trop grande : " + fiche.getTitre());
                    }
                } else {
                    System.out.println("Coordonnées non disponibles pour le WikiDocument : " + wikiId);
                }
            } else {
                System.out.println("Aucune question trouvée pour la fiche : " + fiche.getTitre());
            }
        }

        return nearbyFiches;
    }


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula pour calculer la distance entre deux points sur la surface de la Terre
        final int R = 6371; // Rayon de la Terre en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Retourne la distance en km
    }
}
