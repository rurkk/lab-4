package repositories;

import entity.Owner;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Integer> {
    boolean existsUserByName(String name);
    Optional<User> findUserByName(String name);
    Optional<User> findUserByOwner(Owner owner);
}
