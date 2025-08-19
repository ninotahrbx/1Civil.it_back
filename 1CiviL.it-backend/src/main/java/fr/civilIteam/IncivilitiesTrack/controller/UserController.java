package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationRequest;
import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationResponse;
import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.dto.Users.*;
import fr.civilIteam.IncivilitiesTrack.exception.UserForbiddenException;
import fr.civilIteam.IncivilitiesTrack.service.IUserService;

import fr.civilIteam.IncivilitiesTrack.service.impl.TokenServiceImpl;

import fr.civilIteam.IncivilitiesTrack.service.IValidationTokenService;

import fr.civilIteam.IncivilitiesTrack.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final IUserService iuserService;
    private final TokenServiceImpl tokenService;


    private final IValidationTokenService iValidationTokenService;

    @Secured({ "utilisateur", "admin" })
    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getByuuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userServiceImpl.getByUuid(uuid));
    }

    @Secured({ "utilisateur", "admin" })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceImpl.createUser(userCreateDTO));
    }

    @Secured({ "admin" })
    @PostMapping("/admin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody AdminCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceImpl.createUser(userCreateDTO));
    }

    @Secured({ "utilisateur", "admin" })
    @PutMapping("/{uuid}/{OPuuid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID uuid, @PathVariable UUID OPuuid,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userServiceImpl.updateUser(uuid, OPuuid, userUpdateDTO));
    }

    @Operation(summary = "Allow a user to register")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid UserCreateDTO request) {
        UserDTO response = userServiceImpl.createUser(request);
        log.info("REST request to register user {}", response.getUuid());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Allow a user to log in")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request) {
        log.info("REST request to authenticate user {}", request.email());
        return ResponseEntity.ok(userServiceImpl.authenticate(request));
    }

    /**
     * @param uuid with parameters
     * @body uuid User that made the request my_uuid
     * @return User data
     */
    @Secured({ "utilisateur", "admin" })
    @PostMapping("/{uuid}")
    public ResponseEntity<ResponseUser> getUserByUuid(@PathVariable UUID uuid, @RequestBody RequestUUID req_uuid) {
        UUID my_uuid = req_uuid.uuid();
        ResponseUser responseMyUser = iuserService.getSingleUser(my_uuid);
        if (!responseMyUser.role_name().equalsIgnoreCase("admin") && !my_uuid.equals(uuid)) {
            throw new UserForbiddenException("Access denied: You not have admin privileges.");
        }
        ResponseUser responseUser = iuserService.getSingleUser(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping("/forgot-password/generate")
    // public ResponseEntity<Boolean>generateToken(@RequestBody String email) throws
    // MessagingException, IOException {
    public ResponseEntity<Boolean> generateToken(@RequestBody PasswordRequest request)
            throws MessagingException, IOException {
        return ResponseEntity.ok(tokenService.ForgottenPasswordSendMail(request.getEmail()));
    }

    @PostMapping("/forgot-password/change/{token}")
    public ResponseEntity<Boolean> ChangeToken(@PathVariable String token, @RequestBody PasswordTokenRequest request) {
        return ResponseEntity.ok(userServiceImpl.changeForgottenPassword(token, request));
    }

    @PatchMapping("/users/validate/{token}")
    public ResponseEntity<String> validateAccount(@PathVariable String token) {
        iValidationTokenService.validateToken(token);
        return ResponseEntity.ok("Compte validé avec succès.");
    }
}
