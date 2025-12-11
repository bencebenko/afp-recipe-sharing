package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.Material;
import hu.ekke.receptmegoszto.dto.MaterialDto;
import hu.ekke.receptmegoszto.repository.MaterialRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialServiceTest {

    private final Mockery context = new Mockery();

    @Test
    void getAll_ShouldReturnMappedMaterialDtos() {
        MaterialRepository mockRepo = context.mock(MaterialRepository.class);
        MaterialService service = new MaterialService(mockRepo);

        Material mat = new Material();
        mat.setId(10);
        mat.setName("Liszt");
        mat.setCalories(364.0);
        mat.setProtein(10.0);
        mat.setCarbohydrate(76.0);
        mat.setFat(1.0);
        mat.setUnitOfMeasure("g");

        context.checking(new Expectations() {{
            oneOf(mockRepo).findAll();
            will(returnValue(List.of(mat)));
        }});

        List<MaterialDto> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        MaterialDto dto = result.get(0);
        assertEquals(10L, dto.getId());
        assertEquals("Liszt", dto.getName());
        assertEquals(364.0, dto.getCalories());
        assertEquals(10.0, dto.getProtein());
        assertEquals(76.0, dto.getCarbohydrate());
        assertEquals(1.0, dto.getFat());
        assertEquals("g", dto.getUnitOfMeasure());

        context.assertIsSatisfied();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenRepositoryReturnsEmpty() {
        MaterialRepository mockRepo = context.mock(MaterialRepository.class);
        MaterialService service = new MaterialService(mockRepo);

        context.checking(new Expectations() {{
            oneOf(mockRepo).findAll();
            will(returnValue(List.of()));
        }});

        List<MaterialDto> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        context.assertIsSatisfied();
    }
}
