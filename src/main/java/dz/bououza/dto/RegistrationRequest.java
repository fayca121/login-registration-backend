package dz.bououza.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,max = 20)
    private String password;
}
