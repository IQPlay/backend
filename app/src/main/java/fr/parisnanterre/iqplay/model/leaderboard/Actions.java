package fr.parisnanterre.iqplay.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Actions {
    @JsonProperty("startedOn")
    private Date startedOn;

    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    @Override
    public String toString() {
        return "Actions{" +
                "startedOn=" + startedOn +
                '}';
    }
}