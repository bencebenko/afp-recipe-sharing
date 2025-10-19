package hu.ekke.receptmegoszto.repository;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends CrudRepository<RecipeUser, Long> {

    Optional<RecipeUser> findByUserName(String userName);
}
