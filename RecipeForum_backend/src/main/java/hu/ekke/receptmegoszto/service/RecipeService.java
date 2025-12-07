package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.*;
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

        if (dto.getCategory() != null && dto.getCategory().getId() != null) {
            Long catId = dto.getCategory().getId();
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + catId));
            recipe.setCategory(category);
        }

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            List<Ingredient> ingredients = dto.getIngredients().stream()
                    .map(ingredientDto -> {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setRecipe(recipe);
                        ingredient.setQuantity(ingredientDto.getQuantity());

                        if (ingredientDto.getMaterial() != null && ingredientDto.getMaterial().getId() != null) {
                            Long matId = ingredientDto.getMaterial().getId();
                            Material material = materialRepository.findById(matId)
                                    .orElseThrow(() -> new RuntimeException("Material not found: " + matId));
                            ingredient.setMaterial(material);
                        }

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
    public List<RecipeDto> getByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId).stream()
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
