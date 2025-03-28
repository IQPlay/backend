package fr.parisnanterre.iqplay.wikigame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.parisnanterre.iqplay.wikigame.dto.QcmDTO;
import fr.parisnanterre.iqplay.wikigame.service.AimlApiService;
import info.bliki.wiki.model.WikiModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class WikiGeoSearchController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AimlApiService aimlApiService;

    @GetMapping("/geosearch")
    public ResponseEntity<QcmDTO> getNearestArticle() {
        double lat = 48.858370;
        double lng = 2.294481;

        String searchUrl = "https://fr.wikipedia.org/w/api.php?action=query&list=geosearch" +
                "&gscoord=" + lat + "|" + lng +
                "&gsradius=10000&gslimit=1&format=json";

        try {
            String jsonResponse = restTemplate.getForObject(searchUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            var root = mapper.readTree(jsonResponse);
            var geosearch = root.path("query").path("geosearch");

            if (!geosearch.isArray() || geosearch.size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            var article = geosearch.get(0);
            int pageId = article.path("pageid").asInt();
            String title = article.path("title").asText();

            String contentUrl = "https://fr.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&pageids=" +
                    pageId + "&format=json";
            String contentResponse = restTemplate.getForObject(contentUrl, String.class);
            var contentRoot = mapper.readTree(contentResponse);
            var revisions = contentRoot.path("query").path("pages").path(String.valueOf(pageId)).path("revisions");

            if (!revisions.isArray() || revisions.size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String articleContent = revisions.get(0).path("*").asText();

            WikiModel wikiModel = new WikiModel(
                    "https://fr.wikipedia.org/wiki/${image}",
                    "https://fr.wikipedia.org/wiki/${title}"
            );
            String htmlContent = wikiModel.render(articleContent);

            Document doc = Jsoup.parse(htmlContent);
            doc.select(".infobox, .navbox, .metadata, .reference, .gallery, .mbox, table, .mw-editsection, sup").remove();
            String cleanText = doc.body().text()
                    .replaceAll("\\[\\d+\\]", "")
                    .replaceAll("\\s+", " ");

            String qcmJson = aimlApiService.generateQcm(cleanText);

            QcmDTO qcmDto;
            try {
                qcmDto = mapper.readValue(qcmJson, QcmDTO.class);
            } catch (Exception ex) {
                System.err.println("Erreur de désérialisation du QCM JSON : " + ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            qcmDto.setContent(cleanText);

            return ResponseEntity.ok(qcmDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
