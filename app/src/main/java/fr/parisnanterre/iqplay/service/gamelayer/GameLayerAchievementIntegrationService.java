package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerAchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerAchievementIntegrationService {

    private final GameLayerAchievementService achievementService;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerAchievementIntegrationService() {
        this.achievementService = new GameLayerAchievementService();
    }

    public String getAllAchievements() {
        try {
            HttpResponse<?> response = achievementService.getAllAchievements(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des achievements : " + e.getMessage());
        }
    }

    public String getAchievementById(String achievementId) {
        try {
            HttpResponse<?> response = achievementService.getAchievementById(achievementId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'achievement " + achievementId + " : " + e.getMessage());
        }
    }
}
