package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.dto.RegistrationRequest;

public interface IRegistrationService {
    String register(RegistrationRequest request);
    AppUser confirmToken(String token);

}
