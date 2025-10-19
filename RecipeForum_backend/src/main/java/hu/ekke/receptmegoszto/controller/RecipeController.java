package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {

    private final RecipeMapper recipeMapper;

    private final RecipeService service;

    @PostMapping
    public RecipeDto save(@RequestBody RecipeDto dto) {
        return service.saveRecipe(dto);
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RecipeDto getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
