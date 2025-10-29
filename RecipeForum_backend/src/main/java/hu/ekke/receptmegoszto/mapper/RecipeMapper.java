package hu.ekke.receptmegoszto.mapper;

import hu.ekke.receptmegoszto.domain.Category;
import hu.ekke.receptmegoszto.domain.Ingredient;
import hu.ekke.receptmegoszto.domain.Material;
import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.dto.IngredientDto;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDto toDto(Recipe recipe);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    Recipe toEntity(RecipeDto dto);

    @Mapping(target = "materialId", source = "material.id")
    IngredientDto ingredientToDto(Ingredient ingredient);

    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "material", ignore = true)
    Ingredient ingredientToEntity(IngredientDto dto);

    default Long categoryToId(Category category) {
        return category != null && category.getId() != null ? category.getId().longValue() : null;
    }

    default Long materialToId(Material material) {
        return material != null && material.getId() != null ? material.getId().longValue() : null;
    }
}
