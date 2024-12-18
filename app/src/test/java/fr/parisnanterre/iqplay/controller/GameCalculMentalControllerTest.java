package fr.parisnanterre.iqplay.controller;

import fr.parisnanterre.iqplay.entity.Player;
import fr.parisnanterre.iqplay.service.GameCalculMentalService;
import fr.parisnanterre.iqplay.service.OperationService;
import fr.parisnanterre.iqplay.service.PlayerService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest(GameCalculMentalController.class)
public class GameCalculMentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameCalculMentalService gameService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private OperationService operationService; // Ajout du mock pour OperationService

    @Test
    public void testStartGame_Success() throws Exception {
        // Création d'un joueur réel avec les bonnes données
        Player player = new Player("test@orange.fr", "testPlayer");

        // Simuler la récupération du joueur actuel
        when(playerService.getCurrentPlayer()).thenReturn(player);

        // Simuler la création d'une session et retourner un ID
        Long mockSessionId = 123L;
        when(gameService.getSessionId(any())).thenReturn(mockSessionId);

        // Corps de la requête JSON
        String startGameRequestJson = "{\"difficulty\": 1}";

        // Envoyer la requête POST et vérifier la réponse
        mockMvc.perform(post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(startGameRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("La session a démarré."))
                .andExpect(jsonPath("$.sessionId").value(123));
    }

    @Test
    public void testStartGame_PlayerNotFound() throws Exception {
        // Simuler une exception lorsque le joueur n'est pas trouvé
        when(playerService.getCurrentPlayer()).thenThrow(new IllegalStateException("Authenticated player not found in database."));

        // Corps de la requête JSON
        String startGameRequestJson = "{\"difficulty\": 1}";

        // Envoyer la requête POST et vérifier la réponse d'erreur
        mockMvc.perform(post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(startGameRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Authenticated player not found in database."));
    }
}
