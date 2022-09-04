package vttp.ssf.miniproject.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class RecipeDetails {

    private String imageUrl;
    private int readyInMinutes;
    private int servings;
    private String sourceUrl;
    private List<String> ingredients;
    private List<String> steps;
    private String recipeName;


    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public int getReadyInMinutes() {return readyInMinutes;}
    public void setReadyInMinutes(int readyInMinutes ) {this.readyInMinutes = readyInMinutes;}

    public int getServings() {return servings;}
    public void setServings(int servings) {this.servings = servings;}

    public String getsourceUrl() {return sourceUrl;}
    public void setsourceUrl(String sourceUrl) {this.sourceUrl = sourceUrl;}

    public List<String> getIngredients() {return ingredients;}
    public void setIngredients(List<String> listofIngredients) {this.ingredients= listofIngredients;}

    public List<String> getSteps() {return steps;}
    public void setSteps(List<String> steps ) {this.steps = steps;}

    public String getRecipeName() {return recipeName;}
    public void setRecipeName(String recipeName) {this.recipeName = recipeName;}


    //convert from Json to model objects
    public static RecipeDetails create(JsonObject jo, JsonArray stepsJO, JsonArray extendedJO) {
        RecipeDetails rd  = new RecipeDetails();
        List<String> listOfSteps = new LinkedList<>();
        List<String> listofIngredients = new LinkedList<>();
        rd.setImageUrl(jo.getString("image"));
        rd.setReadyInMinutes(jo.getInt("readyInMinutes"));
        rd.setServings(jo.getInt("servings"));
        rd.setsourceUrl(jo.getString("sourceUrl"));
        
        for (int j = 0; j < stepsJO.size(); j++) {
            JsonObject stepJOIndiv = stepsJO.getJsonObject(j);
            // JsonObject step = stepJOIndiv.getJsonObject("step");
            listOfSteps.add("Step " + (j+1) + ". " + stepJOIndiv.getString("step"));
            // System.out.println(">>>>STEP " + stepJOIndiv.getString("step"));  
        }
        rd.setSteps(listOfSteps);

        for (int k = 0; k < extendedJO.size(); k++) {
            JsonObject originalJOIndiv = extendedJO.getJsonObject(k);
            listofIngredients.add(originalJOIndiv.getString("original"));    
        }

        rd.setIngredients(listofIngredients);
        return rd;
    } 
    
    public static RecipeDetails createNewRecipe(String uploadRecipeName, String uploadMinutes, 
    String uploadServings, String uploadIngredients, String uploadInstructions) {
        RecipeDetails rd  = new RecipeDetails();
        rd.setRecipeName(uploadRecipeName);
        rd.setReadyInMinutes(Integer.parseInt(uploadMinutes));
        rd.setServings(Integer.parseInt(uploadServings));

        List<String> ingredientsList = new LinkedList<>();
        String[] list = uploadIngredients.split("\\|");
        for (String i : list) {
            ingredientsList.add(i);
        }
        rd.setIngredients(ingredientsList);

        
        List<String> instructionList = new LinkedList<>();
        String[] listTwo = uploadInstructions.split("\\|");
        for (String i : listTwo) {
            instructionList.add(i);
        }
        rd.setSteps(instructionList);

        return rd;

    }

    public JsonObject toJson(RecipeDetails h) {
        return Json.createObjectBuilder()
            .add("uploadRN", h.getRecipeName())
            .add("uploadM", h.getReadyInMinutes())
            .add("uploadS", h.getServings())
            .add("uploadI", h.getIngredients().toString())
            .add("uploadSteps", h.getSteps().toString())
            .build();
    }

    public static RecipeDetails recipeDetails(JsonObject displayR) {
        RecipeDetails displayRecipe = new RecipeDetails();
        displayRecipe.setRecipeName(displayR.getString("uploadRN"));
        displayRecipe.setReadyInMinutes(displayR.getInt("uploadM"));
        displayRecipe.setServings(displayR.getInt("uploadS"));

        List<String> ingredientsList = new LinkedList<>();
        String displayIngredients = displayR.getString("uploadI").replaceAll("\\[", "").replaceAll("\\]", "");
        System.out.println("retrieve ingredient:" + displayIngredients);
        String[] list = displayIngredients.split(",");

        for (String i : list) {
            ingredientsList.add(i.trim());
        }
        displayRecipe.setIngredients(ingredientsList);

        
        List<String> instructionList = new LinkedList<>();
        String displaySteps = displayR.getString("uploadSteps").replaceAll("\\[", "").replaceAll("\\]", "");
        String[] listSteps = displaySteps.split(",");

        for (String i : listSteps) {
            instructionList.add(i.trim());
        }

        displayRecipe.setSteps(instructionList);
                
        return displayRecipe;

    }
}
