package dz.bououza.controller;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.dto.ApiResponse;
import dz.bououza.service.IConfirmationTokenService;
import dz.bououza.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
public class ConfirmationTokenController {

    private final IConfirmationTokenService confirmationTokenService;
    private final IRegistrationService registrationService;

    @GetMapping
    public ResponseEntity<ApiResponse> getNewToken(Principal principal){
        AppUser appUser=registrationService.findUserByEmail(principal.getName()).get();
        ConfirmationToken confirmationToken=confirmationTokenService.createNewToken(appUser);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.ok(new ApiResponse(true,
                location+"/confirm?token="+confirmationToken));
    }
}
