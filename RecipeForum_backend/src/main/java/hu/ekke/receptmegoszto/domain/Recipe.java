package hu.ekke.receptmegoszto.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String servings;
    private String duration;

    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<String> steps;
}
