package vttp.ssf.miniproject.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.ssf.miniproject.models.RecipeDetails;
import vttp.ssf.miniproject.services.RecipeDetailsService;

@RestController
@RequestMapping("/buy")
public class RecipeRestController {

    @Autowired
    private RecipeDetailsService rds;

    @GetMapping(path = "/{id}", produces = "text/csv")
    public ResponseEntity<String> getList(@PathVariable(name = "id") int id) {

        RecipeDetails csvFile = rds.getRecipeDetails(Integer.toString(id));
        if (csvFile == null) {
            String errorMsg = "Cannot find recipe %s".formatted(id);
        return ResponseEntity
                // .status(HttpStatus.BAD_REQUEST)
                .badRequest()
                .body(errorMsg);
        }

        List<String> toBuy = csvFile.getIngredients();
        String csvString = toBuy
                .stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(","));
        return ResponseEntity.ok(csvString);
    }
    
}
