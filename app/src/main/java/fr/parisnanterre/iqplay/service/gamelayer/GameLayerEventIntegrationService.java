package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerEventIntegrationService {

    private final GameLayerEventService eventClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerEventIntegrationService() {
        this.eventClient = new GameLayerEventService();
    }

    public String getAllEvents() {
        try {
            HttpResponse<?> response = eventClient.getAllEvents(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    public String getEventById(String eventId) {
        try {
            HttpResponse<?> response = eventClient.getEventById(eventId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'événement " + eventId + " : " + e.getMessage());
        }
    }

    public String completeEvent(String eventId, String username) {
        try {
            HttpResponse<?> response = eventClient.completeEvent(eventId, username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la complétion de l'événement " + eventId + " pour le joueur " + username + " : " + e.getMessage());
        }
    }
}
