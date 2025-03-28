package fr.parisnanterre.iqplay.wikigame.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.parisnanterre.iqplay.wikigame.GeoCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WikiGeoService {

    private final RestTemplate restTemplate;

    public WikiGeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoCoordinates getGeoCoordinates(String wikiId) {
        String apiUrl = "https://fr.wikipedia.org/w/api.php?action=query&prop=coordinates&format=json&pageids=" + wikiId;

        String response = restTemplate.getForObject(apiUrl, String.class);

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonObject pages = jsonResponse.getAsJsonObject("query").getAsJsonObject("pages");
        JsonObject page = pages.getAsJsonObject(wikiId);

        if (page != null && page.has("coordinates")) {
            JsonArray coordinates = page.getAsJsonArray("coordinates");
            JsonObject coord = coordinates.get(0).getAsJsonObject();

            double lat = coord.get("lat").getAsDouble();
            double lon = coord.get("lon").getAsDouble();

            return new GeoCoordinates(lat, lon);
        }

        return null;
    }

}