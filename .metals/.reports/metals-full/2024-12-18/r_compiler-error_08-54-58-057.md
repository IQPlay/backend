file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/controller/PlayerController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 2665
uri: file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/controller/PlayerController.java
text:
```scala
package fr.parisnanterre.iqplay.controller;
import fr.parisnanterre.iqplay.dto.*;
import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.entity.GameSessionPersistante;
import fr.parisnanterre.iqplay.service.PlayerService;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;
    private final GameCalculMentalService GameCalculMentalService;

    @Autowired
    public PlayerController(PlayerService playerService, GameCalculMentalService GameCalculMentalService) {
        this.playerService = playerService;
        this.GameCalculMentalService = GameCalculMentalService;
    }

    /**
     * Route to get the player's profile data.
     *
     * @return ResponseEntity containing player profile data.
     */
    @GetMapping("/profile")
    public ResponseEntity<PlayerProfileDto> getProfile() {
        IPlayer player = playerService.getCurrentPlayer();
        PlayerProfileDto profile = new PlayerProfileDto(
                player.id(),
                ((Player) player).email(),
                ((Player) player).username()
        );
        return ResponseEntity.ok(profile);
    }

    /**
     * Route to get player's statistics (number of games, average score, best score).
     *
     * @return ResponseEntity containing player's statistics.
     */
    @GetMapping("/statistics")
    public ResponseEntity<PlayerStatisticsDto> getStatistics() {
        IPlayer player = playerService.getCurrentPlayer();
        List<GameSessionPersistante> sessions = GameCalculMentalService.getSessionsByPlayer(player);

        int totalGames = sessions.size();
        double averageScore = sessions.stream()
                .mapToInt(GameSessionPersistante::getScore)
                .average()
                .orElse(0.0);
        int bestScore = sessions.stream()
                .mapToInt(GameSessionPersistante::getScore)
                .max()
                .orElse(0);

        PlayerStatisticsDto statistics = new PlayerStatisticsDto(totalGames, averageScore, bestScore);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Route to get the player's game history (finished games and current game if any).
     *
     * @return ResponseE@@ntity containing the game history.
     */
    @GetMapping("/history")
    public ResponseEntity<PlayerGameHistoryDto> getGameHistory() {
        IPlayer player = playerService.getCurrentPlayer();
        List<GameSessionPersistante> sessions = GameCalculMentalService.getSessionsByPlayer(player);

        List<GameSessionDto> finishedGames = sessions.stream()
                .filter(session -> "ENDED".equals(session.getState()))
                .map(session -> new GameSessionDto(
                        session.getId(),
                        session.getName(),
                        session.getLevel(),
                        session.getScore(),
                        session.getState(),
                        session.getCreatedAt()
                ))
                .collect(Collectors.toList());

        List<GameSessionDto> currentGames = sessions.stream()
                .filter(session -> "STARTED".equals(session.getState()))
                .map(session -> new GameSessionDto(
                        session.getId(),
                        session.getName(),
                        session.getLevel(),
                        session.getScore(),
                        session.getState(),
                        session.getCreatedAt()
                ))
                .collect(Collectors.toList());

        PlayerGameHistoryDto history = new PlayerGameHistoryDto(finishedGames, currentGames);
        return ResponseEntity.ok(history);
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator