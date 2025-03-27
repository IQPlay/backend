package fr.parisnanterre.iqplay.model.leaderboard.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.parisnanterre.iqplay.model.leaderboard.Prize;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrizeWrapper {
    private List<Prize> prizes;

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }
}
