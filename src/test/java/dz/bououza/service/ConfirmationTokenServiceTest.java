package dz.bououza.service;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.repository.ConfirmationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ConfirmationTokenServiceTest {

    @MockBean
    private ConfirmationTokenRepository repository;

    private IConfirmationTokenService service;

    @Before
    public void init(){
        service=new ConfirmationTokenServiceImpl(repository);
    }

    @Test
    public void createNewToken_shouldReturnTokenString(){
        when(repository.save(any(ConfirmationToken.class))).thenReturn(any());
        service.createNewToken(new AppUser());
        verify(repository,times(1)).save(any());
    }
}
