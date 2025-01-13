package fr.parisnanterre.iqplay.manager;

import fr.parisnanterre.iqplay.model.GameSession;
import fr.parisnanterre.iqplaylib.api.IGameSession;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveSessionManager {

    private final Map<Long, GameSession> activeSessions = new ConcurrentHashMap<>();

    public Optional<IGameSession> getSession(Long sessionId) {
        return Optional.ofNullable(activeSessions.get(sessionId));
    }

    public void addSession(Long sessionId, GameSession session) {
        activeSessions.put(sessionId, session);
    }

    public void removeSession(Long sessionId) {
        activeSessions.remove(sessionId);
    }
}
