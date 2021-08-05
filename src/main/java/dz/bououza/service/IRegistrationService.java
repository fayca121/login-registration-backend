package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.dto.RegistrationRequest;

import java.util.Optional;

public interface IRegistrationService {
    String register(RegistrationRequest request);
    AppUser confirmToken(String token);
    Optional<AppUser> findUserByEmail(String email);

}
