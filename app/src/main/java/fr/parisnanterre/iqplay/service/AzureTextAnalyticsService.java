package fr.parisnanterre.iqplay.service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

public class AzureTextAnalyticsService {
    private TextAnalyticsClient client;

    public AzureTextAnalyticsService(String endpoint, String apiKey) {
        client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endpoint)
                .buildClient();
    }

    public TextAnalyticsClient getClient() {
        return client;
    }
}
