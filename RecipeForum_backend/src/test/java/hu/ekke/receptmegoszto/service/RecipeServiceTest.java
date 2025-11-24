package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.*;
import hu.ekke.receptmegoszto.dto.IngredientDto;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.dto.MaterialDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.repository.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceTest {

    Mockery context = new Mockery();

    @Test
    void testGetAll_ReturnsMappedDtos() {
        RecipeRepository repo = context.mock(RecipeRepository.class);
        RecipeMapper mapper = context.mock(RecipeMapper.class);
        UserDetailsRepository userRepo = context.mock(UserDetailsRepository.class);
        MaterialRepository materialRepo = context.mock(MaterialRepository.class);
        CategoryRepository categoryRepo = context.mock(CategoryRepository.class);

        RecipeService service = new RecipeService(repo, mapper, userRepo, materialRepo, categoryRepo);

        Recipe recipeEntity = new Recipe();
        RecipeDto recipeDto = new RecipeDto();

        context.checking(new Expectations() {{
            oneOf(repo).findAll();
            will(returnValue(List.of(recipeEntity)));

            oneOf(mapper).toDto(recipeEntity);
            will(returnValue(recipeDto));
        }});

        List<RecipeDto> result = service.getAll();

        assertEquals(1, result.size());
        assertTrue(result.contains(recipeDto));
        context.assertIsSatisfied();
    }

    @Test
    void testSaveRecipe_SavesAndReturnsDto() {
        Mockery ctx = new Mockery();

        RecipeRepository repo = ctx.mock(RecipeRepository.class);
        RecipeMapper mapper = ctx.mock(RecipeMapper.class);
        UserDetailsRepository userRepo = ctx.mock(UserDetailsRepository.class);
        MaterialRepository materialRepo = ctx.mock(MaterialRepository.class);
        CategoryRepository categoryRepo = ctx.mock(CategoryRepository.class);

        RecipeService service = new RecipeService(repo, mapper, userRepo, materialRepo, categoryRepo);

        RecipeDto inputDto = new RecipeDto();
        inputDto.setCategory(new CategoryDto(1L, null));
        IngredientDto ingDto = new IngredientDto();
        // create MaterialDto using no-arg constructor and set id (MaterialDto has many fields now)
        MaterialDto matDto = new MaterialDto();
        matDto.setId(10L);
        ingDto.setMaterial(matDto);
        ingDto.setQuantity("2 cups");
        inputDto.setIngredients(List.of(ingDto));

        Recipe mappedEntity = new Recipe();
        RecipeUser mockUser = new RecipeUser();
        mockUser.setUserName("david");

        Category cat = new Category();
        Material mat = new Material();
        Recipe savedRecipe = new Recipe();
        RecipeDto returnedDto = new RecipeDto();

        Principal principal = () -> "david";

        ctx.checking(new Expectations() {{
            oneOf(mapper).toEntity(inputDto);
            will(returnValue(mappedEntity));

            oneOf(userRepo).findByUserName("david");
            will(returnValue(Optional.of(mockUser)));

            oneOf(categoryRepo).findById(1L);
            will(returnValue(Optional.of(cat)));

            oneOf(materialRepo).findById(10L);
            will(returnValue(Optional.of(mat)));

            oneOf(repo).save(mappedEntity);
            will(returnValue(savedRecipe));

            oneOf(mapper).toDto(savedRecipe);
            will(returnValue(returnedDto));
        }});

        RecipeDto result = service.saveRecipe(inputDto, principal);

        assertSame(returnedDto, result);
        ctx.assertIsSatisfied();
    }

    @Test
    void testDeleteRecipe_DelegatesToRepository() {
        RecipeRepository repo = context.mock(RecipeRepository.class);
        RecipeMapper mapper = context.mock(RecipeMapper.class);
        UserDetailsRepository userRepo = context.mock(UserDetailsRepository.class);
        MaterialRepository materialRepo = context.mock(MaterialRepository.class);
        CategoryRepository categoryRepo = context.mock(CategoryRepository.class);

        RecipeService service = new RecipeService(repo, mapper, userRepo, materialRepo, categoryRepo);

        context.checking(new Expectations() {{
            oneOf(repo).deleteById(42L);
        }});

        service.deleteRecipe(42L);
        context.assertIsSatisfied();
    }
}
