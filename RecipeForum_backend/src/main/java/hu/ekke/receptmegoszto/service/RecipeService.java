package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final RecipeMapper mapper;

    public RecipeService(RecipeRepository repository, RecipeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public RecipeDto saveRecipe(RecipeDto dto) {
        Recipe recipe = mapper.toEntity(dto);
        Recipe saved = repository.save(recipe);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecipeDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }
}
