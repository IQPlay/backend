package fr.parisnanterre.iqplay.generator;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class QcmGenerator {

    private static final String OPENAI_API_URL = "https://iqplay.openai.azure.com/openai/deployments/YOUR_DEPLOYMENT_ID/completions?api-version=2023-03-15-preview";
    private static final String API_KEY = "9SeqGuN6HKGnPyHiZVmfbzGYUwFrJDyHIzCbh1eJnETYZHvPVu9ZJQQJ99BCAC5T7U2XJ3w3AAAAACOGe6nK";  // Remplace par ta clé API Azure OpenAI

    public String generateQcm(String content) {
        String prompt = "Generate a multiple choice question based on the following content: \n\n" + content;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 150);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 1.0);
        requestBody.put("n", 1);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String generatedText = responseJson.path("choices").get(0).path("text").asText();

            return generatedText;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating QCM.";
        }
    }
}

