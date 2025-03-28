package fr.parisnanterre.iqplay.wikigame;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AimlApiService {

    private static final String API_URL = "https://api.aimlapi.com/v1/chat/completions";
    @Value("${api.aimlapikey}")
    private String apiKey;

    private final OkHttpClient client;

    public AimlApiService() {
        this.client = new OkHttpClient();
    }

    /**
     * Génère un QCM structuré en JSON basé sur le contenu fourni.
     *
     * La structure attendue est :
     * {
     *   "content": "<texte nettoyé>",
     *   "qcm": [
     *     {
     *       "question": "<question>",
     *       "reponses": [
     *         { "reponse": "<réponse1>", "correct": true/false },
     *         { "reponse": "<réponse2>", "correct": true/false },
     *         ...
     *       ]
     *     },
     *     ...
     *   ]
     * }
     *
     * @param content Le contenu nettoyé de l'article.
     * @return Le QCM généré sous forme de JSON.
     * @throws IOException
     * @throws JSONException
     */
    public String generateQcm(String content) throws IOException, JSONException {
        System.out.println("api key : " + this.apiKey);
        // Préparez un prompt détaillé pour obtenir la structure souhaitée
        String prompt = "Génère une seule question pertinente sous forme de QCM basée sur le texte suivant :\n" +
                "{\n" +
                "  \"content\": \"" + content + "\",\n" +
                "  \"qcm\": [\n" +
                "    {\n" +
                "      \"question\": \"<question bien formulée>\",\n" +
                "      \"reponses\": [\n" +
                "         { \"reponse\": \"<réponse 1>\", \"correct\": false },\n" +
                "         { \"reponse\": \"<réponse 2>\", \"correct\": true },\n" +
                "         { \"reponse\": \"<réponse 3>\", \"correct\": false },\n" +
                "         { \"reponse\": \"<réponse 4>\", \"correct\": false }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "La question doit être formulée sous forme interrogative complète et porter sur un élément clé du texte " +
                "(événement marquant, date importante, personnalité, lieu, construction, etc.). " +
                "Elle doit être claire, informative et intéressante pour l'apprentissage.\n" +
                "Les réponses doivent être crédibles et bien construites, avec une seule réponse correcte parmi les quatre proposées.\n" +
                "IMPORTANT :\n" +
                "- Exclure toute question liée aux images, fichiers, galeries ou descriptions de photos (exemple : Fichier:Construction_tour_eiffel.jpg).\n" +
                "- Ne pas utiliser d'expressions comme 'mentionné dans le texte', 'd'après le paragraphe', 'comme indiqué plus haut', etc. La question doit être naturelle et indépendante.\n" +
                "- La question ne doit pas être trop simple ni trop difficile, et elle doit permettre un apprentissage intéressant sur le sujet.";


        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("model", "mistralai/Mistral-7B-Instruct-v0.2");
        jsonRequest.put("temperature", 0.7);

        JSONArray messages = new JSONArray()
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", "Tu es un assistant spécialisé dans la création de QCM."))
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", prompt));
        jsonRequest.put("messages", messages);

        RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur lors de la requête à l'API AIML : " + response);
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
    }
}
