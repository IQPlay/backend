package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerStreakService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerStreakIntegrationService {

    private final GameLayerStreakService streakClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerStreakIntegrationService() {
        this.streakClient = new GameLayerStreakService();
    }

    public String getAllStreaks() {
        try {
            HttpResponse<?> response = streakClient.getAllStreaks(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des streaks : " + e.getMessage());
        }
    }

    public String getStreakById(String streakId) {
        try {
            HttpResponse<?> response = streakClient.getStreakById(streakId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération du streak " + streakId + " : " + e.getMessage());
        }
    }
}
