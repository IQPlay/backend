package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerMissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerMissionIntegrationService {

    private final GameLayerMissionService missionClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerMissionIntegrationService() {
        this.missionClient = new GameLayerMissionService();
    }

    public String getAllMissions() {
        try {
            HttpResponse<?> response = missionClient.getAllMissions(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des missions : " + e.getMessage());
        }
    }

    public String getMissionById(String missionId) {
        try {
            HttpResponse<?> response = missionClient.getMissionById(missionId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération de la mission " + missionId + " : " + e.getMessage());
        }
    }
}
