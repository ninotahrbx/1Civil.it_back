package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.Users.PasswordTokenRequest;
import fr.civilIteam.IncivilitiesTrack.exception.TokenExpiredException;
import fr.civilIteam.IncivilitiesTrack.exception.UserNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final IUserRepository userRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    private final long expiration_Time =3600000 ;

    private final EmailServiceImpl emailService;
    /**
     * Genere un token d'une heure si l'email correspond a un user en base
     * @param email
     * @return
     */
    @Override
    public String generateToken(String email) {


        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration_Time))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public boolean ForgottenPasswordSendMail(String email) throws MessagingException, IOException {
        User user =userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());

        HashMap<String ,String > map = new HashMap<>();
        String token = generateToken(email);

        user.setTokenPassword(token);

        userRepository.save(user);
        map.put("token", token);

        emailService.sendHtmlEmail(email,"ForgottenPassword",map) ;

        return true;
    }

    /**
     * valide le token et extrait l'email
     * @param token
     * @return
     */
    @Override
    public String extractEmailFromToken(String token) {
        Claims claims =Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        }
        catch (RuntimeException e )
        {
            return  false ;
        }
    }


}
