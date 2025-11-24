package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.dto.MaterialDto;
import hu.ekke.receptmegoszto.domain.Material;
import hu.ekke.receptmegoszto.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository repository;

    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MaterialDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private MaterialDto toDto(Material m) {
        if (m == null) return null;
        Long id = m.getId() != null ? m.getId().longValue() : null;
        return new MaterialDto(id, m.getName(), m.getCalories(), m.getProtein(), m.getCarbohydrate(), m.getFat(), m.getUnitOfMeasure());
    }
}

