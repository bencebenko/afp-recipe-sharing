package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService service;

    @Test
    void getAll_ShouldReturnListOfRecipes() throws Exception {
        RecipeDto dto = new RecipeDto(1L, "Pizza", null, 10, 20, 2,
                "Tésztás", "Sütés 20 perc", new CategoryDto(3L, null), List.of());
        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].portion").value(2));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoRecipes() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getById_ShouldReturnRecipe() throws Exception {
        RecipeDto dto = new RecipeDto(2L, "Leves", null, 5, 15, 4,
                "Finom", "Keverés", new CategoryDto(1L, null), List.of());
        when(service.getById(2L)).thenReturn(dto);

        mockMvc.perform(get("/recipe/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Leves"))
                .andExpect(jsonPath("$.portion").value(4));

        verify(service, times(1)).getById(2L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void save_ShouldReturnSavedRecipe() throws Exception {
        RecipeDto dto = new RecipeDto(3L, "Gulyás", null, 20, 60, 6,
                "Marhahúsos", "Főzés 1 óra", new CategoryDto(2L, null), List.of());
        when(service.saveRecipe(any(RecipeDto.class), any(Principal.class))).thenReturn(dto);

        Principal principal = () -> "testuser";

        mockMvc.perform(post("/recipe")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Gulyás",
                              "prepTime": 20,
                              "cookTime": 60,
                              "portion": 6,
                              "description": "Marhahúsos"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Gulyás"))
                .andExpect(jsonPath("$.portion").value(6));

        verify(service, times(1)).saveRecipe(any(RecipeDto.class), eq(principal));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getByCategory_ShouldReturnFilteredRecipes() throws Exception {
        Long categoryId = 3L;
        RecipeDto dto = new RecipeDto(1L, "Pizza", null, 10, 20, 2,
                "Tésztás", "Sütés", new CategoryDto(categoryId, "Főételek"), List.of());
        when(service.getByCategory(categoryId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/recipe/bycategory")
                        .param("categoryId", String.valueOf(categoryId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].category.id").value(categoryId));

        verify(service, times(1)).getByCategory(categoryId);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getByCategory_ShouldReturnBadRequest_WhenCategoryIdMissing() throws Exception {
        mockMvc.perform(get("/recipe/bycategory"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void delete_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/recipe/99"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteRecipe(99L);
        verifyNoMoreInteractions(service);
    }
}
