package fr.civilIteam.IncivilitiesTrack.controller;


import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.civilIteam.IncivilitiesTrack.dto.Users.PasswordRequest;
import fr.civilIteam.IncivilitiesTrack.dto.Users.PasswordTokenRequest;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserCreateDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserDTO;
import fr.civilIteam.IncivilitiesTrack.exception.NotSameEmailTokenException;
import fr.civilIteam.IncivilitiesTrack.exception.TokenExpiredException;
import fr.civilIteam.IncivilitiesTrack.exception.UserNotFoundException;
import fr.civilIteam.IncivilitiesTrack.service.impl.TokenServiceImpl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserCreateDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserDTO;
import fr.civilIteam.IncivilitiesTrack.exception.AccountAlreadyVerifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TokenExpiredException;
import fr.civilIteam.IncivilitiesTrack.exception.UserForbiddenException;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import fr.civilIteam.IncivilitiesTrack.service.IValidationTokenService;

import fr.civilIteam.IncivilitiesTrack.service.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IValidationTokenService iValidationTokenService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TokenServiceImpl tokenService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID uuid;
    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        uuid = UUID.randomUUID();
        userDTO = new UserDTO();
        userDTO.setUuid(uuid);
        userCreateDTO = new UserCreateDTO();
    }

    @Test
    void getByUuid_ShouldReturnUserDTO_WhenUserExists() throws Exception {
        when(userService.getByUuid(uuid)).thenReturn(userDTO);

        mockMvc.perform(get("/users/{uuid}", uuid))
            .andExpect(status().isOk());
    }

    @Test
    void createUser_ShouldReturnCreatedUserDTO() throws Exception {
        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userCreateDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    void generateTocken_shouldBeOk() throws MessagingException, IOException {
        when(tokenService.ForgottenPasswordSendMail(Mockito.anyString())).thenReturn(true);
        PasswordRequest request=new PasswordRequest("test@test.com");

        ResponseEntity<Boolean> response =userController.generateToken(request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(true,response.getBody());
    }

    @Test
    void generateTocken_souldBeUserNotFound () throws MessagingException, IOException {
        PasswordRequest request=new PasswordRequest("test@test.com");

        when(tokenService.ForgottenPasswordSendMail(Mockito.anyString())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()-> userController.generateToken(request));

    }

    @Test
    void changeToken_shouldBeOK ()
    {
        String token = "testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("pass");

        when (userService.changeForgottenPassword(token,body)).thenReturn(true);

        ResponseEntity<Boolean>response = userController.ChangeToken(token,body);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(true,response.getBody());


    }

    @Test
    void changeToken_shouldBeUserNotFound () {

        String token = "testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("pass");

        when (userService.changeForgottenPassword(token,body)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()->userController.ChangeToken(token,body));
    }

    @Test
    void changeToken_shouldBeTokenExpired() {

        String token = "testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("pass");

        when (userService.changeForgottenPassword(token,body)).thenThrow(TokenExpiredException.class);

        assertThrows(TokenExpiredException.class,()->userController.ChangeToken(token,body));
    }

    @Test
    void changeToken_shouldNotSameEmail() {

        String token = "testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("pass");

        when (userService.changeForgottenPassword(token,body)).thenThrow(NotSameEmailTokenException.class);

        assertThrows(NotSameEmailTokenException.class,()->userController.ChangeToken(token,body));
    }


@Test
    public void testGetUserByUuid_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        RequestUUID reqUuid = new RequestUUID(uuid);
        ResponseUser responseUser = new ResponseUser(uuid,"Doe","John","johndoe@gmail.com","0605203178","admin");

        when(userService.getSingleUser(uuid)).thenReturn(responseUser);

        ResponseEntity<ResponseUser> responseEntity = userController.getUserByUuid(uuid, reqUuid);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseUser, responseEntity.getBody());
    }

    @Test
    public void testGetUserByUuid_AccessDenied() throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID differentUuid = UUID.randomUUID();
        RequestUUID reqUuid = new RequestUUID(differentUuid);
        ResponseUser responseMyUser = new ResponseUser(uuid,"Doe","Jane","janedoe@gmail.com","07775533","utilisateur");

        when(userService.getSingleUser(differentUuid)).thenReturn(responseMyUser);

        assertThrows(UserForbiddenException.class, () -> {
            userController.getUserByUuid(uuid, reqUuid);
        });
    }

    @Test
    void shouldValidateAccountSuccessfullyWhenTokenIsValid() {

        // Given

        String token = "valid-token";
        doNothing().when(iValidationTokenService).validateToken(token);

        // When

        ResponseEntity<String> response = userController.validateAccount(token);

        // Then

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Compte validé avec succès.", response.getBody());
    }

    @Test
    void shouldThrowAccountAlreadyVerifiedExceptionWhenAccountAlreadyVerified() {

        String token = "valid-token";
        doThrow(new AccountAlreadyVerifiedException("Le compte est déjà vérifié")).when(iValidationTokenService).validateToken(token);

        AccountAlreadyVerifiedException exception = assertThrows(AccountAlreadyVerifiedException.class,
            () -> userController.validateAccount(token));

        assertEquals("Le compte est déjà vérifié", exception.getMessage());
    }

    @Test
    void shouldThrowTokenExpiredExceptionWhenTokenIsExpired() {


        String token = "expired-token";
        doThrow(new TokenExpiredException("Le token de validation a expiré.")).when(iValidationTokenService).validateToken(token);


        TokenExpiredException exception = assertThrows(TokenExpiredException.class,
            () -> userController.validateAccount(token));

        assertEquals("Le token de validation a expiré.", exception.getMessage());
    }

}