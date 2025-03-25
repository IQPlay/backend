package fr.parisnanterre.iqplay.service.gamelayer;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplaylib.gamelayer.GameLayerPlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class GameLayerPlayerIntegrationService {

    private final GameLayerPlayerService playerClient;

    @Value("${gamelayer.account:agence-recherche}")
    private String account;

    public GameLayerPlayerIntegrationService() {
        this.playerClient = new GameLayerPlayerService();
    }

    public String createPlayer(Player player) {
        try {
            HttpResponse<?> response = playerClient.createPlayer(
                    player.username(), 0, 0, player.username(), account
            );
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la création du joueur GameLayer : " + e.getMessage());
        }
    }

    public String deletePlayer(Player player) {
        try {
            HttpResponse<?> response = playerClient.deletePlayerById(player.username(), account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur lors de la suppression du joueur GameLayer : " + e.getMessage());
        }
    }

    public String getPlayerInfo(String username) {
        try {
            HttpResponse<?> response = playerClient.getPlayerById(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération joueur GameLayer : " + e.getMessage());
        }
    }

    public String getAchievements(String username) {
        try {
            HttpResponse<?> response = playerClient.getAchivementsByPlayer(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération achievements joueur : " + e.getMessage());
        }
    }

    public String getEvents(String username) {
        try {
            HttpResponse<?> response = playerClient.getEventsByPlayer(username, account);
            return response.body().toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur récupération événements joueur : " + e.getMessage());
        }
    }

    public String getLevels(String username) {
        try {
            HttpResponse<?> response = playerClient.getLevelsByPlayer(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération niveaux joueur : " + e.getMessage());
        }
    }

    public String getMissions(String username) {
        try {
            HttpResponse<?> response = playerClient.getMissionByPlayerId(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération missions joueur : " + e.getMessage());
        }
    }

    public String getMission(String username, String missionId) {
        try {
            HttpResponse<?> response = playerClient.getMissionByPlayerIdAndMissionId(username, account, missionId);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération mission " + missionId + " : " + e.getMessage());
        }
    }

    public String getPrizes(String username) {
        try {
            HttpResponse<?> response = playerClient.getPrizesByPlayer(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération récompenses joueur : " + e.getMessage());
        }
    }

    public String getRanking(String username) {
        try {
            HttpResponse<?> response = playerClient.getRankingByPlayer(username, account);
            return response.body().toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur récupération classement joueur : " + e.getMessage());
        }
    }

    public String getStreak(String username) {
        try {
            HttpResponse<?> response = playerClient.getPlayerStreak(username, account);
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erreur récupération streak joueur : " + e.getMessage());
        }
    }
}
