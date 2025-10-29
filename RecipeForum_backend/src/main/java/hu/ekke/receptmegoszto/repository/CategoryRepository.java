package hu.ekke.receptmegoszto.repository;

import hu.ekke.receptmegoszto.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}


