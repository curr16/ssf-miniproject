package vttp.ssf.miniproject.repositories;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.miniproject.models.RecipeDetails;

@Repository
public class RecipeRepo {

    @Autowired
    @Qualifier("template")
    private RedisTemplate<String, String> redisTemplate;

    public void saveUserRecipe(RecipeDetails rd){

        String newRecipeName = rd.getRecipeName();

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(newRecipeName, rd.toJson(rd).toString());

        if (!redisTemplate.hasKey("list")) {
            List<String> allRecipes = new LinkedList<>();

            allRecipes.add(newRecipeName);
            
            valueOp.set("list", allRecipes.toString());

        } else {

            List<String> usersList = new LinkedList<>();
            String retrievedRecipes = valueOp.get("list").replaceAll("\\[", "").replaceAll("\\]", "").trim();
            
            String[] retrievedEmails = retrievedRecipes.split(",");

            for (String s: retrievedEmails) {
                usersList.add(s);
            }
            usersList.add(newRecipeName);
            valueOp.set("list", usersList.toString());
        }
    }
    
    public RecipeDetails displayRecipe(String name) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();

        String jsonString = valueOp.get(name);
        System.out.println(jsonString);

        Reader strReader = new StringReader(jsonString);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject jObject = jsonReader.readObject();
        return RecipeDetails.recipeDetails(jObject);
    }

    public List<String> getAllRecipes() {

        List<String> allRecipes = new LinkedList<>();

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String existingRecipe = valueOp.get("list").replaceAll("\\[", "").replaceAll("\\]", "").trim();
        String[] individualRecipe = existingRecipe.split(",");

        for (String i : individualRecipe) {

            String recipe = i.trim();
            
            allRecipes.add(recipe);
        }
        return allRecipes;
    }


}
