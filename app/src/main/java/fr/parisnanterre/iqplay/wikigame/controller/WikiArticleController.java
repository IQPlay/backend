package fr.parisnanterre.iqplay.wikigame.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.parisnanterre.iqplay.wikigame.dto.WikiArticleDTO;
import info.bliki.wiki.model.WikiModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class WikiArticleController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/wiki")
    public ResponseEntity<WikiArticleDTO> getArticleByUrl(@RequestParam String url) {
        try {
            String title = extractTitleFromUrl(url);
            if (title == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String idQueryUrl = "https://fr.wikipedia.org/w/api.php?action=query&titles=" + encodedTitle + "&format=json";
            String idResponse = restTemplate.getForObject(idQueryUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(idResponse);
            JsonNode pages = root.path("query").path("pages");
            if (pages.isEmpty() || pages.has("-1")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String pageId = pages.fieldNames().next();
            long wikiId = Long.parseLong(pageId);

            System.out.println("Wiki ID: " + wikiId);

            String contentQueryUrl = "https://fr.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext=true&pageids=" + wikiId + "&format=json";
            String contentResponse = restTemplate.getForObject(contentQueryUrl, String.class);
            JsonNode contentRoot = mapper.readTree(contentResponse);
            String content = contentRoot.path("query").path("pages").path(pageId).path("extract").asText();

            String cleanText = cleanWikipediaText(content);
            return ResponseEntity.ok(new WikiArticleDTO(wikiId, url, title, cleanText));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String extractTitleFromUrl(String url) {
        if (url == null || !url.contains("/wiki/")) {
            return null;
        }
        return url.substring(url.lastIndexOf("/wiki/") + 6).replace("_", " ");
    }

    private String cleanWikipediaText(String content) throws IOException {
        if (content == null || content.isEmpty()) {
            return "";
        }

        WikiModel wikiModel = new WikiModel(
                "https://fr.wikipedia.org/wiki/${image}",
                "https://fr.wikipedia.org/wiki/${title}"
        );

        String htmlContent = wikiModel.render(content);
        Document doc = Jsoup.parse(htmlContent);
        doc.select(".infobox, .navbox, .metadata, .reference, .gallery, .mbox, table, .mw-editsection, sup").remove();

        return doc.body().text().replaceAll("\\[\\d+\\]", "").replaceAll("\\s+", " ");
    }
}

