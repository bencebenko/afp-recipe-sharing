package hu.ekke.receptmegoszto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDto {
    private Long id;
    private String name;
    private Double calories;
    private Double protein;
    private Double carbohydrate;
    private Double fat;
    private String unitOfMeasure;
}
