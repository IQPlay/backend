package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerLevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerLevelIntegrationService {

    private final GameLayerLevelService levelClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerLevelIntegrationService() {
        this.levelClient = new GameLayerLevelService();
    }

    public String getAllLevels() {
        try {
            HttpResponse<?> response = levelClient.getAllLevels(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des niveaux : " + e.getMessage());
        }
    }

    public String getLevelById(String levelId) {
        try {
            HttpResponse<?> response = levelClient.getLevelById(levelId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération du niveau " + levelId + " : " + e.getMessage());
        }
    }
}
