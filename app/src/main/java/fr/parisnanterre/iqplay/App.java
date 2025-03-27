package fr.parisnanterre.iqplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"fr.parisnanterre.iqplay", "fr.parisnanterre.iqplaylib"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
