package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService service;

    @Test
    void getAll_ShouldReturnListOfCategories() throws Exception {
        CategoryDto dto1 = new CategoryDto(1L, "Főételek");
        CategoryDto dto2 = new CategoryDto(2L, "Desszertek");
        List<CategoryDto> categoryList = List.of(dto1, dto2);

        when(service.getAll()).thenReturn(categoryList);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Főételek"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Desszertek"));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoCategories() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }
}
