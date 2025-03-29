package fr.parisnanterre.iqplay.wikigame.dto.fiche.fetch;

public class FicheRequestDTO {
    private Long playerId;
    private double lat;
    private double lon;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
