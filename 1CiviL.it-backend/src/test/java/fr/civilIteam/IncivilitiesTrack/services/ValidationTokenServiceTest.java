package fr.civilIteam.IncivilitiesTrack.services;

import fr.civilIteam.IncivilitiesTrack.exception.AccountAlreadyVerifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TokenExpiredException;
import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.impl.ValidationTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class ValidationTokenServiceTest {

    @Mock
    private IUserRepository iUserRepository;

    @InjectMocks
    private ValidationTokenServiceImpl validationTokenService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setTokenValidate("valid-token");
    }

    @Test
    void shouldThrowAccountAlreadyVerifiedExceptionWhenUserAlreadyVerified() {

        user.setTokenValidate(null);
        when(iUserRepository.findByTokenValidate("valid-token")).thenReturn(Optional.of(user));

        AccountAlreadyVerifiedException exception = assertThrows(AccountAlreadyVerifiedException.class, () -> {
            validationTokenService.validateToken("valid-token");
        });

        assertEquals("Le compte est déjà vérifié", exception.getMessage());
    }

    @Test
    void shouldThrowTokenExpiredExceptionWhenTokenIsExpired() {

        user.setTokenValidate("expired-token");
        when(iUserRepository.findByTokenValidate("valid-token")).thenReturn(Optional.empty());

        TokenExpiredException exception = assertThrows(TokenExpiredException.class, () -> {
            validationTokenService.validateToken("valid-token");
        });

        assertEquals("Le token de validation a expiré.", exception.getMessage());
    }

    @Test
    void shouldValidateUserWhenTokenIsValid() {

        when(iUserRepository.findByTokenValidate("valid-token")).thenReturn(Optional.of(user));

        validationTokenService.validateToken("valid-token");

        verify(iUserRepository, times(1)).save(user);
        assertNull(user.getTokenValidate());
    }
}
