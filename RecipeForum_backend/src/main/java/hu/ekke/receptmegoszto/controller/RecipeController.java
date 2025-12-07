package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService service;

    @PostMapping
    public RecipeDto save(@RequestBody RecipeDto dto, Principal principal) {
        return service.saveRecipe(dto, principal);
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/bycategory")
    public List<RecipeDto> getByCategory(@RequestParam Long categoryId) {
        return service.getByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public RecipeDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRecipe(id);
    }
}
