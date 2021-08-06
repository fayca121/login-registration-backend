package dz.bououza.service;

import dz.bououza.repository.ConfirmationTokenRepository;
import dz.bououza.domain.AppUser;
import dz.bououza.domain.AppUserRole;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.dto.RegistrationRequest;
import dz.bououza.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService{
    private final AppUserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IConfirmationTokenService confirmationTokenService;

    @Override
    public String register(RegistrationRequest request){
        if(userRepository.existsByEmail(request.getEmail()))
            return null;
        AppUser appUser=new AppUser();
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(AppUserRole.USER);
        AppUser savedUser=userRepository.save(appUser);
        ConfirmationToken savedToken=confirmationTokenService.createNewToken(savedUser);
        return savedToken.getToken();
    }

    @Override
    @Transactional
    public AppUser confirmToken(String token) {
        if(token==null)
            throw new IllegalArgumentException("Token should not be empty or null");
        Optional<ConfirmationToken> optionalToken=confirmationTokenRepository.findConfirmationTokenByToken(token);
        ConfirmationToken confirmationToken=optionalToken.orElseThrow(()->
                new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt()!=null)
            throw new IllegalStateException("token is already used");
        if(LocalDateTime.now().isAfter(confirmationToken.getExpiredAt()))
            throw new IllegalStateException("token is expired, please ask for new token");
        AppUser appUser=confirmationToken.getAppUser();
        if(appUser.getEnabled())
            throw new IllegalStateException("registration has already confirmed");
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        appUser.setEnabled(true);
        return userRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> findUserByEmail(String email) {
        return userRepository.findAppUserByEmail(email);
    }
}
