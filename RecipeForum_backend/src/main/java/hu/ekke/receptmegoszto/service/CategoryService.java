package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.domain.Category;
import hu.ekke.receptmegoszto.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private CategoryDto toDto(Category c) {
        if (c == null) return null;
        Long id = c.getId() != null ? c.getId().longValue() : null;
        return new CategoryDto(id, c.getName());
    }
}

