package dz.bououza.controller;

import dz.bououza.domain.AppUser;
import dz.bououza.domain.ConfirmationToken;
import dz.bououza.dto.ApiResponse;
import dz.bououza.dto.TokenRequest;
import dz.bououza.dto.TokenResponse;
import dz.bououza.service.IConfirmationTokenService;
import dz.bououza.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
@Log4j2
public class ConfirmationTokenController {

    private final IConfirmationTokenService confirmationTokenService;
    private final IRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<TokenResponse> getNewToken(@RequestBody TokenRequest tokenRequest){
        AppUser appUser=registrationService.findUserByEmail(tokenRequest.getEmail()).get();
        ConfirmationToken confirmationToken=confirmationTokenService.createNewToken(appUser);
        TokenResponse tokenResponse=new TokenResponse();
        tokenResponse.setEmail(appUser.getEmail());
        tokenResponse.setToken(confirmationToken.getToken());
        tokenResponse.setCreatedAt(confirmationToken.getCreatedAt());
        return ResponseEntity.ok(tokenResponse);
    }
}
