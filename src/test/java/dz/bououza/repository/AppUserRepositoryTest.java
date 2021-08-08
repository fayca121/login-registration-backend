package dz.bououza.repository;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.AppUserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository repository;

    @Test
    public void findAppUserByEmail_shouldReturn1User(){
        AppUser user=new AppUser();
        user.setEmail("user1@mail.com");
        user.setFirstName("User1");
        user.setPassword("p@ssw0rd");
        user.setRole(AppUserRole.USER);
        entityManager.persist(user);
        entityManager.flush();

        AppUser appUser=repository.findAppUserByEmail("user1@mail.com").get();
        assertThat(appUser.getId()).isNotNull();
        assertThat(appUser.getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    public void existsByEmail_shouldReturnTrue(){
        AppUser user=new AppUser();
        user.setEmail("user2@mail.com");
        user.setFirstName("User2");
        user.setPassword("p@ssw0rd");
        user.setRole(AppUserRole.USER);
        entityManager.persist(user);
        entityManager.flush();
        assertThat(repository.existsByEmail("user2@mail.com")).isTrue();
    }

    @Test(expected = ConstraintViolationException.class)
    public void incorrectEmail_shouldGenerateException(){
        AppUser user=new AppUser();
        user.setEmail("user3mail.com");
        user.setFirstName("User3");
        user.setPassword("p@ssw0rd");
        user.setRole(AppUserRole.USER);
        entityManager.persist(user);
        entityManager.flush();
    }


}
