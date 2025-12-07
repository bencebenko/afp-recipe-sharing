package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.CategoryDto;
import hu.ekke.receptmegoszto.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(operationId = "getAllCategories", summary = "List all categories") // <--- EZ A LÉNYEG
    public List<CategoryDto> getAll() {
        return service.getAll();
    }
}

