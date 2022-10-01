package vttp.ssf.miniproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf.miniproject.models.RecipeDetails;
import vttp.ssf.miniproject.models.Recipes;
import vttp.ssf.miniproject.models.User;
import vttp.ssf.miniproject.services.RecipeDetailsService;
import vttp.ssf.miniproject.services.RecipeService;
import vttp.ssf.miniproject.services.UserService;

@Controller
@RequestMapping
public class RecipeController {

    @Autowired
    private RecipeService recipeSvc;

    @Autowired
    private RecipeDetailsService recipeDetailsSvc;

    @Autowired
    private UserService uService;

    @PostMapping (path = "/register")
    public String getUser(@RequestBody MultiValueMap<String, String> form, Model model) {
        
        String name = form.getFirst("name");
        String email = form.getFirst("email");
        String password = form.getFirst("password");

        if (uService.checkUserExist(email)) {
            return "login"; 
        }

        User newUser = new User();
        newUser = newUser.create(name, email, password);
        uService.saveUserDetail(newUser);
        return "recipeSearch";

    }
            
    
    @PostMapping (path = "/login")
    public String getLogin(@RequestBody MultiValueMap<String, String> form, Model model) {
        String loginEmail = form.getFirst("username");
        String loginPassword = form.getFirst("password");

        User existingUser = uService.getUserDetail(loginEmail);

        if (existingUser == null) {
            
            System.out.println("user did not exist");
            String userError = "user did not exist";
            model.addAttribute("userError", userError);
            return "login";

        }

        if (BCrypt.checkpw(loginPassword, existingUser.getPassword())) {
            System.out.println("Password matches");
            return "recipeSearch";

        } else {

            String error = "WRONG PASSWORD!!";
            // System.out.println("Password does not match");
            model.addAttribute("error", error);
            return "login";
        }

    }


    //create recipe form
    @PostMapping (path = "/create")
    public String getsavedRecipe(@RequestBody MultiValueMap<String, String> form, Model model) {

        String uploadRecipeName = form.getFirst("recipeName");
        String uploadMinutes = form.getFirst("readyInMinutes");
        String uploadServings = form.getFirst("servings");
        String uploadIngredients = form.getFirst("ingredients");
        String uploadInstructions = form.getFirst("instructions");

        RecipeDetails rd = RecipeDetails.createNewRecipe(uploadRecipeName, uploadMinutes, uploadServings, uploadIngredients, uploadInstructions);
        recipeDetailsSvc.saveNewUserRecipe(rd);
        
        return null;

    }

    @GetMapping(path = "/ingredients")
    public String getRecipes(Model model, @RequestParam String ingredients) {

        List<Recipes> recipes = recipeSvc.getRecipes(ingredients);
      
        model.addAttribute("list", recipes);
        return "recipeSearch";

    }

    @GetMapping(path = "/display")
    public String getRecipeDetails(Model model, @RequestParam String id) {

        RecipeDetails retrieveRD = recipeDetailsSvc.getRecipeDetails(id);

        model.addAttribute("retrieveRD", retrieveRD);
        return "view";
    }

    @GetMapping(path = "/{name}")
    public String getdisplayRecipe(Model model, @PathVariable(name = "name") String name) {
        RecipeDetails getDetails = recipeDetailsSvc.callDisplayRecipe(name);
        model.addAttribute("displayR", getDetails);

        return "displayRecipe";
    }

    @RequestMapping("/register")
    public String registerPage() {
        return "index";
    }
    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }  
    
    @RequestMapping(path = "/savedRecipes")
    public String getdisplayRecipe(Model model) {
        
        List<String> allRecipes = recipeDetailsSvc.tableofRecipe();
        
        model.addAttribute("allRecipes", allRecipes);

        return "createdRecipe";
    }
}
    

