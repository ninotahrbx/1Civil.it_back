package fr.civilIteam.IncivilitiesTrack.services;

import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationRequest;
import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationResponse;
import fr.civilIteam.IncivilitiesTrack.dto.Users.*;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IUserMapper;
import fr.civilIteam.IncivilitiesTrack.exception.*;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IRoleRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.impl.TokenServiceImpl;
import fr.civilIteam.IncivilitiesTrack.service.impl.UserServiceImpl;
import fr.civilIteam.IncivilitiesTrack.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserMapper userMapper;

    @Mock
    private TokenServiceImpl tokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new User();
        user.setUuid(uuid);
        user.setEmail("test@example.com");
        user.setPassword("Password@123");
    }

    @Test
    void getByUuid_ShouldReturnUserDTO_WhenUserExists() {
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        when(userMapper.usertoUserDTO(user)).thenReturn(new UserDTO(uuid, "test@example.com", "John", "Doe", "0123456789", new Date(), null, null, new Role(1L, UUID.randomUUID(), "name", null )));

        UserDTO result = userService.getByUuid(uuid);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getByUuid_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getByUuid(uuid));
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "Password@123");
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");

        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authenticationToken);

        AuthenticationResponse response = userService.authenticate(request);
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.accessToken());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("unknown@example.com", "Password@123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.authenticate(request));
    }

    @Test
    void authenticate_ShouldThrowException_WhenAuthenticationFails() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrongpassword");
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
//        doThrow(AuthenticationException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(authenticationManager.authenticate(authenticationToken)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.authenticate(request));
    }

    @Test
    void changeForgottenPassword_souldBeOK()
    {
        String token="testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("test");
        User user =  new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", passwordEncoder.encode("admin"), "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);

        when (userRepository.findByTokenPassword(token)).thenReturn(Optional.of(user));
        when (tokenService.isTokenValid(token)).thenReturn(true);
        when(tokenService.extractEmailFromToken(token)).thenReturn(user.getEmail());
        Boolean response = userService.changeForgottenPassword(token,body);

        assertNotNull(response);
        assertEquals(true ,response);


    }

    @Test
    void changeForgottenPassword_UserNotFound() {
        String token="testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("test");
        User user =  new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", passwordEncoder.encode("admin"), "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);

        when (userRepository.findByTokenPassword(token)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()->userService.changeForgottenPassword(token,body));


    }

    @Test
    void changeForgottenPassword_TokenExpired()
    {
        String token="testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("test");
        User user =  new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", passwordEncoder.encode("admin"), "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);

        when (userRepository.findByTokenPassword(token)).thenReturn(Optional.of(user));
        when (tokenService.isTokenValid(token)).thenReturn(false);

        assertThrows(TokenExpiredException.class,()->userService.changeForgottenPassword(token,body));


    }

    @Test
    void changeForgotenPassword_NotSameEmailToken()
    {
        String token="testToken";
        PasswordTokenRequest body = new PasswordTokenRequest();
        body.setPassword("test");
        String fakeEmail="Fake@Test.com";
        User user =  new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", passwordEncoder.encode("admin"), "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);

        when (userRepository.findByTokenPassword(token)).thenReturn(Optional.of(user));
        when (tokenService.isTokenValid(token)).thenReturn(true);
        when(tokenService.extractEmailFromToken(token)).thenReturn(fakeEmail);

        assertThrows(NotSameEmailTokenException.class,()->userService.changeForgottenPassword(token,body));


    }
}
