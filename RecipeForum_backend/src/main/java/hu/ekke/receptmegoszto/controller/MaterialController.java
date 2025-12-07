package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.dto.MaterialDto;
import hu.ekke.receptmegoszto.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService service;

    public MaterialController(MaterialService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(operationId = "getAllMaterials", summary = "List all materials")
    public List<MaterialDto> getAll() {
        return service.getAll();
    }
}

