package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/public")
    public RecipeDto getRecipe() {
        return getRecipeDto();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/secret")
    public RecipeDto getSecretRecipe() {
        return getRecipeDto();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/verysecret")
    public RecipeDto getVerySecretRecipe(@PathVariable Long id) {
        return getRecipe();
    }

    private RecipeDto getRecipeDto() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Gulyásleves");
        recipe.setServings("Kb. 6 adag");
        recipe.setDuration("~90 perc");
        recipe.setIngredients(List.of(
                "600 g marhalábszár kockázva",
                "2 ek olaj vagy zsír",
                "2 közepes vöröshagyma, 3 gerezd fokhagyma",
                "2 ek édes pirospaprika",
                "1-1 sárgarépa és fehérrépa, fél zeller",
                "2 közepes burgonya",
                "1 paradicsom, 1 zöldpaprika",
                "1 tk kömény, 1 babérlevél, só, bors",
                "1,5–2 l víz/alaplé"
        ));
        recipe.setSteps(List.of(
                "Hagymát zsiradékon üvegesre, mehet a fokhagyma.",
                "Lehúzva a tűzről pirospaprika, vissza, hozzá a hús, fehéredésig pirít.",
                "Fűszerek (kömény, babér, só, bors), felöntöd vízzel, fedő alatt 60 perc lassú főzés.",
                "Zöldségek (répák, zeller, paradicsom, paprika) → további 15 perc.",
                "Burgonya hozzá → még 15 perc, amíg minden puha.",
                "Ízesítés finomhangolása, friss kenyérrel tálalás."
        ));

        return recipeMapper.toDto(recipe);
    }
}
