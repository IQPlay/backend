package fr.parisnanterre.iqplay.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.parisnanterre.iqplay.dto.QcmDTO;
import fr.parisnanterre.iqplay.generator.QcmGenerator;
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
    private QcmGenerator qcmGenerator;
    @Autowired
    private RestTemplate restTemplate;

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
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode geosearch = root.path("query").path("geosearch");

            if (geosearch.isArray() && geosearch.size() > 0) {
                JsonNode article = geosearch.get(0);
                int pageId = article.path("pageid").asInt();
                String title = article.path("title").asText();

                String contentUrl = "https://fr.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&pageids=" +
                        pageId + "&format=json";

                String contentResponse = restTemplate.getForObject(contentUrl, String.class);
                JsonNode contentRoot = mapper.readTree(contentResponse);
                JsonNode revisions = contentRoot.path("query").path("pages").path(String.valueOf(pageId)).path("revisions");

                QcmDTO dto = new QcmDTO();
                if (revisions.isArray() && revisions.size() > 0) {
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

                    dto.setContent(cleanText);

                    String qcm = qcmGenerator.generateQcm(cleanText);

                    dto.setQcm(qcm);

                    return ResponseEntity.ok(dto);
                }

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
