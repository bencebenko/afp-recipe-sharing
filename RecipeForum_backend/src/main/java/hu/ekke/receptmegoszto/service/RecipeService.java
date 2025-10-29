package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.dto.RecipeDto;
import hu.ekke.receptmegoszto.mapper.RecipeMapper;
import hu.ekke.receptmegoszto.repository.RecipeRepository;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final RecipeMapper mapper;
    private final UserDetailsRepository userRepository;

    public RecipeService(RecipeRepository repository, RecipeMapper mapper, UserDetailsRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public RecipeDto saveRecipe(RecipeDto dto, Principal principal) {
        Recipe recipe = mapper.toEntity(dto);

        RecipeUser user = userRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        recipe.setUser(user);

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

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }
}
