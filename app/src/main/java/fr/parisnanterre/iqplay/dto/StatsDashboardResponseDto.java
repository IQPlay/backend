package fr.parisnanterre.iqplay.dto;

public class StatsDashboardResponseDto {
    private int nbGames;
    private int avgScore;
    private int bestScore;
    private int serie;

    public StatsDashboardResponseDto(int nbGames, int avgScore, int bestScore, int serie) {
        this.nbGames = nbGames;
        this.avgScore = avgScore;
        this.bestScore = bestScore;
        this.serie = serie;
    }

    public int getNbGames() {
        return nbGames;
    }

    public void setNbGames(int nbGames) {
        this.nbGames = nbGames;
    }

    public int getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }
}
