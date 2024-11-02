package fr.parisNanterre.FideliTech;

import fr.parisNanterre.FideliTech.models.Operation;
import fr.parisNanterre.FideliTech.services.OperationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    // Bean CommandLineRunner pour exécuter du code après le démarrage de l'application
    @Bean
    public CommandLineRunner run(OperationService operationService) {
        return args -> {
            int difficulty = 12; // Définissez le niveau de difficulté souhaité
            Operation operation = operationService.createOperation(difficulty);
            System.out.println("Operation: " + operation.getExpression() + " = " + operation.getResult());
        };
    }
}
