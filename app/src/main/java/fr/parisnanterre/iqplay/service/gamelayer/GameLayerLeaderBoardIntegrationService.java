package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplaylib.gamelayer.GameLayerLeaderBoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerLeaderBoardIntegrationService {

    private final GameLayerLeaderBoardService leaderboardClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerLeaderBoardIntegrationService() {
        this.leaderboardClient = new GameLayerLeaderBoardService();
    }

    public String getAllLeaderboards() {
        try {
            HttpResponse<?> response = leaderboardClient.getAllLeaderboards(account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération des leaderboards : " + e.getMessage());
        }
    }

    public String getLeaderboardById(String leaderboardId) {
        try {
            HttpResponse<?> response = leaderboardClient.getLeaderboardById(leaderboardId, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la récupération du leaderboard " + leaderboardId + " : " + e.getMessage());
        }
    }
}
