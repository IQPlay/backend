package fr.parisnanterre.iqplay.config;

import fr.parisnanterre.iqplay.model.GameCalculMental;
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


    @Bean
    public GameCalculMental gameCalculMental(OperationService operationService) {
        return new GameCalculMental("Calcul Mental", operationService);
    }

}
