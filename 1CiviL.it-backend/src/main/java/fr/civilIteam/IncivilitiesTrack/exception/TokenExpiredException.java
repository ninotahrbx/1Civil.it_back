package fr.civilIteam.IncivilitiesTrack.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GATEWAY_TIMEOUT, reason = "Token expired")
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);

    }
    public TokenExpiredException() {
        super("Token expired");

    }
}
