package vttp.ssf.miniproject.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Recipes {

    private int id;
    private String title;
    private String imageUrl;
    private String diet;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getDiet() {return diet;}
    public void setDiet(String diet) {this.diet = diet;}

    //convert from Json to model objects
    public static Recipes create(JsonObject jo) {
        Recipes r  = new Recipes();
        r.setId(jo.getInt("id"));
        r.setTitle(jo.getString("title"));
        r.setImageUrl(jo.getString("image"));
        // r.setDiet(jo.getString("diet"));
        return r;
    }
    
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("id", id)
            .add("title", title)
            .add("imageUrl", imageUrl)
            .add("diet", diet)
            .build();
    }
    
}



