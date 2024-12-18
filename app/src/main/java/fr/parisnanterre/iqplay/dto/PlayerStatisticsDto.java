package fr.parisnanterre.iqplay.dto;

public class PlayerStatisticsDto {
    private int totalGames;
    private double averageScore;
    private int bestScore;

    public PlayerStatisticsDto(int totalGames, double averageScore, int bestScore) {
        this.totalGames = totalGames;
        this.averageScore = averageScore;
        this.bestScore = bestScore;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
}
