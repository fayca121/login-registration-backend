package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;

public interface IConfirmationTokenService {
    ConfirmationToken createNewToken(AppUser user);
}
