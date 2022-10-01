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
import vttp.ssf.miniproject.repositories.RecipeRepo;
import vttp.ssf.miniproject.models.RecipeDetails;

@Service
public class RecipeDetailsService {

    @Autowired
    private RecipeRepo rr;

    private static final String url = "https://api.spoonacular.com/recipes/";
    
    @Value("${x-api-key}")
    private String key;

    public RecipeDetails getRecipeDetails(String numberID) {

        String payload;

        // Create url with query string (add parameters)
        String uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(numberID, "information")
                .toUriString();
        // System.out.println(">>>>>> URI" + uri);

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
            JsonObject recipesDetailsJO = jsonReader.readObject();

            JsonArray analyzedJO = recipesDetailsJO.getJsonArray("analyzedInstructions");
            JsonArray extendedJO = recipesDetailsJO.getJsonArray("extendedIngredients");

            // List<String> list = new LinkedList<>();
            RecipeDetails rd = new RecipeDetails();

            for (int i = 0; i < analyzedJO.size(); i++) {
                JsonObject jo = analyzedJO.getJsonObject(i);
                // JsonObject originalJO = extendedJO.getJsonObject(i);

                JsonArray stepsJO = jo.getJsonArray("steps");
                rd = RecipeDetails.create(recipesDetailsJO, stepsJO, extendedJO);
                
            }
            return rd;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;      
    }

    // save created recipe
    public void saveNewUserRecipe(RecipeDetails rd) {
        rr.saveUserRecipe(rd);
    }

    public RecipeDetails callDisplayRecipe(String name) {
        return rr.displayRecipe(name);
    }

    public List<String> tableofRecipe() {
        return rr.getAllRecipes();
    }


}


