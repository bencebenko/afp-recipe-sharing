package hu.ekke.receptmegoszto.repository;

import hu.ekke.receptmegoszto.domain.MyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends CrudRepository<MyUser, Long> {

    Optional<MyUser> findByUserName(String userName);
}
