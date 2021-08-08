package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.dto.RegistrationRequest;
import dz.bououza.repository.AppUserRepository;
import dz.bououza.repository.ConfirmationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RegistrationServiceTest {

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private ConfirmationTokenRepository tokenRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private IConfirmationTokenService confirmationTokenService;

    private IRegistrationService registrationService;

    private final String TOKEN="PbprG3c2FcioN94uYYlDHROkewyLcs1DmvC76bYHNqXrQgAto8VegrgfixBhMMdStvLcaT0YeJynL" +
            "mBcpin2Ssz7HPfLegD0HwV7Zx66gD5jPCjDddawLse9lRLgL9rO";

    @Before
    public void init(){
        registrationService= new RegistrationServiceImpl(appUserRepository,tokenRepository,
                passwordEncoder,confirmationTokenService);
    }

    @Test
    public void registerNewUser_shouldReturn1User(){
        RegistrationRequest request=new RegistrationRequest();
        request.setEmail("user1@mail.com");
        request.setFirstName("User1");
        request.setLastName("UserL");
        request.setPassword("P@ssw0rd");
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        when(appUserRepository.existsByEmail("user1@mail.com")).thenReturn(false);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(new AppUser());
        when(confirmationTokenService.createNewToken(any(AppUser.class))).thenReturn(confirmationToken);
        when(passwordEncoder.encode(any())).thenReturn("");

        String generatedToken=registrationService.register(request);

        verify(appUserRepository,times(1)).existsByEmail("user1@mail.com");
        verify(appUserRepository,times(1)).save(any(AppUser.class));
        verify(passwordEncoder,times(1)).encode(any());
        verify(confirmationTokenService,times(1)).createNewToken(any(AppUser.class));
        assertThat(generatedToken).isEqualTo(TOKEN);

    }

    @Test
    public void registerExistingUser_shouldReturnNull(){
        RegistrationRequest request=new RegistrationRequest();
        request.setEmail("user1@mail.com");
        request.setFirstName("User1");
        request.setLastName("UserL");
        request.setPassword("P@ssw0rd");
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        when(appUserRepository.existsByEmail("user1@mail.com")).thenReturn(true);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(new AppUser());

        String generatedToken=registrationService.register(request);
        verify(appUserRepository,times(1)).existsByEmail("user1@mail.com");
        verify(appUserRepository,times(0)).save(any(AppUser.class));

        assertThat(generatedToken).isNull();

    }

    @Test
    public void confirmToken_shouldEnableUser(){
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.MAX);
        AppUser appUser=new AppUser();
        appUser.setEmail("user1@mail.com");
        appUser.setFirstName("user1");
        appUser.setLastName("user1L");
        appUser.setPassword("p@ssw0rd");
        confirmationToken.setAppUser(appUser);
        when(tokenRepository.findConfirmationTokenByToken(TOKEN)).thenReturn(Optional.of(confirmationToken));
        when(tokenRepository.save(confirmationToken)).thenReturn(any());
        when(appUserRepository.save(appUser)).thenReturn(appUser);

        AppUser result= registrationService.confirmToken(TOKEN);
        verify(tokenRepository,times(1)).findConfirmationTokenByToken(TOKEN);
        verify(tokenRepository,times(1)).save(confirmationToken);
        verify(appUserRepository,times(1)).save(appUser);

        assertThat(result.getEnabled()).isTrue();

    }

    @Test(expected = IllegalStateException.class)
    public void confirmTokenAlreadyConfirmed_shouldReturnException(){
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.MAX);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        when(tokenRepository.findConfirmationTokenByToken(TOKEN)).thenReturn(Optional.of(confirmationToken));
        when(tokenRepository.save(confirmationToken)).thenReturn(any());
        registrationService.confirmToken(TOKEN);
        verify(tokenRepository,times(1)).findConfirmationTokenByToken(TOKEN);
    }

    @Test(expected = IllegalStateException.class)
    public void confirmTokenHasExpired_shouldReturnException(){
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        confirmationToken.setCreatedAt(LocalDateTime.now().minusMinutes(15L));
        confirmationToken.setExpiredAt(LocalDateTime.now().minusMinutes(1L));
        when(tokenRepository.findConfirmationTokenByToken(TOKEN)).thenReturn(Optional.of(confirmationToken));
        when(tokenRepository.save(confirmationToken)).thenReturn(any());
        registrationService.confirmToken(TOKEN);
        verify(tokenRepository,times(1)).findConfirmationTokenByToken(TOKEN);
    }

    @Test(expected = IllegalStateException.class)
    public void confirmTokenUserEnabled_shouldReturnException(){
        ConfirmationToken confirmationToken=new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.MAX);
        AppUser appUser=new AppUser();
        appUser.setEmail("user1@mail.com");
        appUser.setFirstName("user1");
        appUser.setLastName("user1L");
        appUser.setPassword("p@ssw0rd");
        appUser.setEnabled(true);
        confirmationToken.setAppUser(appUser);
        when(tokenRepository.findConfirmationTokenByToken(TOKEN)).thenReturn(Optional.of(confirmationToken));
        when(tokenRepository.save(confirmationToken)).thenReturn(any());
        registrationService.confirmToken(TOKEN);
        verify(tokenRepository,times(1)).findConfirmationTokenByToken(TOKEN);
        verify(tokenRepository,times(0)).save(confirmationToken);
    }

}
