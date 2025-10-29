package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.*;
import hu.ekke.receptmegoszto.dto.IngredientDto;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final RecipeMapper mapper;
    private final UserDetailsRepository userRepository;
    private final MaterialRepository materialRepository;
    private final CategoryRepository categoryRepository;

    public RecipeService(RecipeRepository repository, RecipeMapper mapper,
                         UserDetailsRepository userRepository,
                         MaterialRepository materialRepository,
                         CategoryRepository categoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.materialRepository = materialRepository;
        this.categoryRepository = categoryRepository;
    }

    public RecipeDto saveRecipe(RecipeDto dto, Principal principal) {
        Recipe recipe = mapper.toEntity(dto);

        RecipeUser user = userRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        recipe.setUser(user);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));
            recipe.setCategory(category);
        }

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            List<Ingredient> ingredients = dto.getIngredients().stream()
                    .map(ingredientDto -> {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setRecipe(recipe);
                        ingredient.setQuantity(ingredientDto.getQuantity());

                        Material material = materialRepository.findById(ingredientDto.getMaterialId())
                                .orElseThrow(() -> new RuntimeException("Material not found: " + ingredientDto.getMaterialId()));
                        ingredient.setMaterial(material);

                        return ingredient;
                    })
                    .collect(Collectors.toList());
            recipe.setIngredients(ingredients);
        } else {
            recipe.setIngredients(new ArrayList<>());
        }

        Recipe saved = repository.save(recipe);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecipeDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }
}
