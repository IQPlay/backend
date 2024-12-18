file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/service/PlayerService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/Users/mathi/Documents/github/IQPLay/backend/app/src/main/java/fr/parisnanterre/iqplay/service/PlayerService.java
text:
```scala
package fr.parisnanterre.iqplay.service;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.repository.PlayerRepository;
import fr.parisnanterre.iqplaylib.api.IPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, JwtBlacklistService jwtBlacklistService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    /**
     * Enregistre un nouveau joueur dans la base de données.
     *
     * @param email    L'email du joueur.
     * @param username Le nom d'utilisateur du joueur.
     * @param password Le mot de passe non encodé du joueur.
     * @return Le joueur enregistré.
     */
    public Player registerPlayer(String email, String username, String password) {
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException("Email, username, and password cannot be null.");
        }

        if (playerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (playerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use.");
        }

        Player player = new Player();
        player.email(email);
        player.username(username);
        player.password(passwordEncoder.encode(password));

        return playerRepository.save(player);
    }

    /**
     * Authentifie un joueur en vérifiant son email et son mot de passe.
     *
     * @param email    L'email du joueur.
     * @param password Le mot de passe non encodé fourni par l'utilisateur.
     * @return Le joueur authentifié.
     */
    public Player authenticatePlayer(String email, String password) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No player found with this email."));

        if (!passwordEncoder.matches(password, player.password())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return player;
    }

    /**
     * Récupère le joueur actuellement authentifié.
     *
     * @return Le joueur actuellement connecté.
     */
    public IPlayer getCurrentPlayer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No authenticated player found.");
        }

        String username = authentication.getName(); // Supposant que l'email est utilisé comme principal
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated player not found in database."));
    }

    /**
     * Invalide un token JWT et déconnecte le joueur.
     *
     * @param token Le token à invalider.
     */
    public void logout(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty.");
        }
        jwtBlacklistService.invalidateToken(token);
        SecurityContextHolder.clearContext();
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
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:345)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:109)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator