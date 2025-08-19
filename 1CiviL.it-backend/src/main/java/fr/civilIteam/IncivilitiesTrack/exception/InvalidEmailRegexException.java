package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Your Email does no match a valid one ")
public class InvalidEmailRegexException extends RuntimeException {
    public InvalidEmailRegexException(String message) {
        super(message);
    }
    public InvalidEmailRegexException(){super("Your Email does no match a valid one "); }
}
