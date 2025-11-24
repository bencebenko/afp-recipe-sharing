package hu.ekke.receptmegoszto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;
    private String name;
    private String imageRef;
    private Integer prepTime;
    private Integer cookTime;
    private Integer portion;
    private String description;
    private String preparationSteps;
    private CategoryDto category;
    private List<IngredientDto> ingredients;
}
