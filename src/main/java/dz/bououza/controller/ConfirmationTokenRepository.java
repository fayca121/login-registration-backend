package dz.bououza.controller;

import dz.bououza.domain.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}