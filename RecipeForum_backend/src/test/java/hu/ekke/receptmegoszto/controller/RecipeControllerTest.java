package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//Git megj.
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
                "Tésztás", "Sütés 20 perc", 3L, List.of());
        Mockito.when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].portion").value(2));
    }

    @Test
    void getById_ShouldReturnRecipe() throws Exception {
        RecipeDto dto = new RecipeDto(2L, "Leves", null, 5, 15, 4,
                "Finom", "Keverés", 1L, List.of());
        Mockito.when(service.getById(2L)).thenReturn(dto);

        mockMvc.perform(get("/recipe/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Leves"))
                .andExpect(jsonPath("$.portion").value(4));
    }

    @Test
    void save_ShouldReturnSavedRecipe() throws Exception {
        RecipeDto dto = new RecipeDto(3L, "Gulyás", null, 20, 60, 6,
                "Marhahúsos", "Főzés 1 óra", 2L, List.of());
        Mockito.when(service.saveRecipe(any(RecipeDto.class), any())).thenReturn(dto);

        mockMvc.perform(post("/recipe")
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
                .andExpect(jsonPath("$.name").value("Gulyás"))
                .andExpect(jsonPath("$.portion").value(6));
    }

    @Test
    void delete_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/recipe/99"))
                .andExpect(status().isOk());
        Mockito.verify(service).deleteRecipe(eq(99L));
    }
}
