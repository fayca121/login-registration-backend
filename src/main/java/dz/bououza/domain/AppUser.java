package dz.bououza.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean enabled=false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AppUserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != AppUser.class)
            return false;
        AppUser appUser = (AppUser) o;

        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
