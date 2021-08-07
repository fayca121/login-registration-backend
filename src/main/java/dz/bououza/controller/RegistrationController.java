package dz.bououza.controller;

import dz.bououza.domain.AppUser;
import dz.bououza.dto.ApiResponse;
import dz.bououza.dto.RegistrationRequest;
import dz.bououza.service.IRegistrationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/registration")
@Log4j2
public class RegistrationController {

    private final IRegistrationService registrationService;

    public RegistrationController(IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest registrationRequest){
        String registeredToken= registrationService.register(registrationRequest);
        if(registeredToken == null)
            return ResponseEntity.badRequest().body(new ApiResponse(false,"The email is already used"));
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.ok(new ApiResponse(true,
                "Successfully registered, "+location+"/confirm?token="+registeredToken));
    }

    @GetMapping("confirm")
    public ResponseEntity<ApiResponse> confirm(@RequestParam("token")String token){
        log.info("token: "+token);
        AppUser appUser=registrationService.confirmToken(token);
        if(appUser.getEnabled())
          return ResponseEntity.ok(new ApiResponse(true,"Registration confirmed"));
        else
            return ResponseEntity.badRequest().body(new ApiResponse(true,"Registration confirmed"));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> IllegalStateExceptionHandler(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,e.getMessage()));
    }
}
