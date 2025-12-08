package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.MaterialDto;
import hu.ekke.receptmegoszto.service.MaterialService;
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

@WebMvcTest(controllers = MaterialController.class)
@AutoConfigureMockMvc(addFilters = false)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialService service;

    @Test
    void getAll_ShouldReturnListOfMaterials() throws Exception {
        MaterialDto dto1 = new MaterialDto(1L, "Liszt", 0.0, 0.0, 0.0, 0.0, "g");
        MaterialDto dto2 = new MaterialDto(2L, "Tojás", 0.0, 0.0, 0.0, 0.0, "db");
        List<MaterialDto> materialList = List.of(dto1, dto2);

        when(service.getAll()).thenReturn(materialList);

        mockMvc.perform(get("/material"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Liszt"))
                .andExpect(jsonPath("$[0].unitOfMeasure").value("g"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tojás"))
                .andExpect(jsonPath("$[1].unitOfMeasure").value("db"));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoMaterials() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/material"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).getAll();
        verifyNoMoreInteractions(service);
    }
}
