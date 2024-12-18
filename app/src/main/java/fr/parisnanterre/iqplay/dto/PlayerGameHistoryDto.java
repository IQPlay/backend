package fr.parisnanterre.iqplay.dto;

import java.util.List;

public class PlayerGameHistoryDto {
    private List<GameSessionDto> finishedGames;
    private List<GameSessionDto> currentGames;

    public PlayerGameHistoryDto(List<GameSessionDto> finishedGames, List<GameSessionDto> currentGames) {
        this.finishedGames = finishedGames;
        this.currentGames = currentGames;
    }

    public List<GameSessionDto> getFinishedGames() {
        return finishedGames;
    }

    public void setFinishedGames(List<GameSessionDto> finishedGames) {
        this.finishedGames = finishedGames;
    }

    public List<GameSessionDto> getCurrentGames() {
        return currentGames;
    }

    public void setCurrentGames(List<GameSessionDto> currentGames) {
        this.currentGames = currentGames;
    }
}
