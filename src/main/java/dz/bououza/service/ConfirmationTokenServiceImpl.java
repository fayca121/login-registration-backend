package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.repository.ConfirmationTokenRepository;
import dz.bououza.token.SecureTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements IConfirmationTokenService{

    private final ConfirmationTokenRepository repository;

    @Override
    public ConfirmationToken createNewToken(AppUser user) {
        //generation of new token for validation mail
        ConfirmationToken token=new ConfirmationToken();
        token.setToken(SecureTokenGenerator.nextToken());
        token.setAppUser(user);
        return repository.save(token);
    }
}
