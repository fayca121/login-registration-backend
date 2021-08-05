package dz.bououza.repository;

import dz.bououza.domain.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser,Long> {
    Optional<AppUser> findAppUserByEmail(String email);
    Boolean existsByEmail(String email);
}
