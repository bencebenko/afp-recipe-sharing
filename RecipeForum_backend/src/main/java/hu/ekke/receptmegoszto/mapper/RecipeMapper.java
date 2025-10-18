package hu.ekke.receptmegoszto.mapper;

import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeDto toDto(Recipe recipe);
    Recipe toEntity(RecipeDto dto);
}
