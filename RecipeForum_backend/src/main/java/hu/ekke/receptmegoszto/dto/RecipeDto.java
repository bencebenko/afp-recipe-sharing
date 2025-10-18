package hu.ekke.receptmegoszto.dto;

import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        String servings,
        String duration,
        List<String> ingredients,
        List<String> steps
) {}
