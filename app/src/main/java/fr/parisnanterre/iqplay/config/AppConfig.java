package fr.parisnanterre.iqplay.config;

import fr.parisnanterre.iqplay.entity.GameCalculMental;
import fr.parisnanterre.iqplay.service.OperationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining application beans.
 *
 * This class is annotated with @Configuration to indicate that it provides
 * Spring configuration. It defines beans for the OperationService and
 * GameCalculMental classes, which are used in the application for generating
 * mathematical operations and managing game sessions, respectively.
 *
 * @see OperationService
 * @see GameCalculMental
 */
@Configuration
public class AppConfig {

    /**
     * Configuration class for defining application beans.
     *
     * This class is annotated with @Configuration to indicate that it provides
     * Spring configuration. It defines beans for the OperationService and
     * GameCalculMental classes, which are used in the application for generating
     * mathematical operations and managing game sessions, respectively.
     *
     * @see OperationService
     * @see GameCalculMental
     */
    @Bean
    public OperationService operationService() {
        return new OperationService();
    }

    /**
     * Defines a Spring bean for the GameCalculMental class.
     *
     * This method creates and returns a GameCalculMental instance,
     * injecting the provided OperationService to facilitate the
     * generation of mathematical operations within the game.
     *
     * @param operationService the service used for creating mathematical operations
     * @return a GameCalculMental instance configured with the specified OperationService
     * @see GameCalculMental
     * @see OperationService
     */
    @Bean
    public GameCalculMental gameCalculMental(OperationService operationService) {
        return new GameCalculMental("Calcul Mental", operationService);
    }
}
