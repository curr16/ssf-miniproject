package vttp.ssf.miniproject.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.miniproject.models.Recipes;

@Service
public class RecipeService {

    private static final String url = "https://api.spoonacular.com/recipes/complexSearch";
    
    @Value("${x-api-key}")
    private String key;

    public List<Recipes> getRecipes(String ingredients) {

        String payload;

        // Create url with query string (add parameters)
        String uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", ingredients)
                .toUriString();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;


        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", key);

            HttpEntity<String> request = new HttpEntity<>(headers);

            // resp = template.exchange(url, method, requestEntity, responseType, uriVariables)
            resp = template.exchange(uri, HttpMethod.GET, request, String.class, 1);

            payload = resp.getBody();
            // System.out.println(">>> PAYLOAD: " + payload);

            // Convert payload into JsonObject
            // Convert string to a Reader
            Reader strReader = new StringReader(payload);

            // Create a JsonReader from reader
            JsonReader jsonReader = Json.createReader(strReader);
    
            // Read the payload as Json object
            JsonObject recipesResult = jsonReader.readObject();
    
           //should tally with the object name from api
            JsonArray resultList = recipesResult.getJsonArray("results");
    
            List<Recipes> list = new LinkedList<>();
            for (int i = 0; i < resultList.size(); i++) {
                JsonObject jo = resultList.getJsonObject(i);
    
                list.add(Recipes.create(jo));
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
}


