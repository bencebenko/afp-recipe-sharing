package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.Category;
import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.repository.CategoryRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private final Mockery context = new Mockery();

    @Test
    void getAll_ShouldReturnMappedCategoryDtos() {
        CategoryRepository mockRepo = context.mock(CategoryRepository.class);
        CategoryService service = new CategoryService(mockRepo);

        Category cat1 = new Category();
        cat1.setId(1);
        cat1.setName("Levesek");

        Category cat2 = new Category();
        cat2.setId(2);
        cat2.setName("Főételek");

        List<Category> categories = List.of(cat1, cat2);

        context.checking(new Expectations() {{
            oneOf(mockRepo).findAll();
            will(returnValue(categories));
        }});

        List<CategoryDto> result = service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        CategoryDto dto1 = result.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Levesek", dto1.getName());

        CategoryDto dto2 = result.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Főételek", dto2.getName());

        context.assertIsSatisfied();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenRepositoryReturnsEmpty() {
        CategoryRepository mockRepo = context.mock(CategoryRepository.class);
        CategoryService service = new CategoryService(mockRepo);

        context.checking(new Expectations() {{
            oneOf(mockRepo).findAll();
            will(returnValue(List.of()));
        }});

        List<CategoryDto> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        context.assertIsSatisfied();
    }
}
