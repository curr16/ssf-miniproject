package vttp.ssf.miniproject.repositories;

import java.io.Reader;
import java.io.StringReader;

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
    }
    
    public void displayRecipe(String name) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String jsonString = valueOp.get(name);

        Reader strReader = new StringReader(jsonString);
            JsonReader jsonReader = Json.createReader(strReader);
            JsonObject jObject = jsonReader.readObject();
            RecipeDetails.recipeDetails(jObject);


    }
}
