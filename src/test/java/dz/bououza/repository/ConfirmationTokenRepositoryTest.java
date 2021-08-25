package dz.bououza.repository;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.AppUserRole;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.token.SecureTokenGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConfirmationTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConfirmationTokenRepository repository;

    @Test
    public void findConfirmationTokenByToken_shouldReturn1Token(){
        //create user for foreingkey
        AppUser user=new AppUser();
        user.setEmail("user1@mail.com");
        user.setFirstName("User1");
        user.setPassword("p@ssw0rd");
        user.setRole(AppUserRole.USER);
        entityManager.persist(user);

        ConfirmationToken token=new ConfirmationToken();
        String testToken= SecureTokenGenerator.nextToken();
        token.setToken(testToken);
        token.setAppUser(user);
        entityManager.persist(token);
        entityManager.flush();

        ConfirmationToken saveToken=repository.findConfirmationTokenByToken(testToken).get();
        assertThat(saveToken.getId()).isNotNull();
        assertThat(saveToken.getToken()).isEqualTo(testToken);
    }
}
