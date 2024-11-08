package fr.parisNanterre.iqPlay;
import fr.parisNanterre.iqPlay.services.Evaluator;
import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        io.sentry.Sentry.init(options -> {
            options.setDsn("https://50f2f952963626f8b3be5810204d4f2a@o4505313118453760.ingest.us.sentry.io/4508251108802561");
            options.setTracesSampleRate(1.0);
            options.setDebug(true);
        });

        try {
            SpringApplication.run(App.class, args);
        } catch (Exception e) {
            Sentry.captureException(e);
        }

    }
}
