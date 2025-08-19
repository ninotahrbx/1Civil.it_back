package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.IValidationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.civilIteam.IncivilitiesTrack.exception.AccountAlreadyVerifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TokenExpiredException;

@Service
@RequiredArgsConstructor
public class ValidationTokenServiceImpl implements IValidationTokenService {

    private final IUserRepository iUserRepository;

    @Transactional
    @Override
    public void validateToken(String token) {
        User user = iUserRepository.findByTokenValidate(token)
                .orElseThrow(() -> new TokenExpiredException("Le token de validation a expiré."));

        if (user.getTokenValidate() == null || user.getTokenValidate().isEmpty()) {
            throw new AccountAlreadyVerifiedException("Le compte est déjà vérifié");
        }

        user.setTokenValidate(null);  // Active le compte
        iUserRepository.save(user);
    }

}
