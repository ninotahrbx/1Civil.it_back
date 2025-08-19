package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Your password must have a minimum size of 12 and have at least 1 letter, 1 capital letter, 1 number and 1 special character")
public class InvalidPasswordRegexException extends RuntimeException {
    public InvalidPasswordRegexException(String message) {
        super(message);
    }
    public InvalidPasswordRegexException() {
        super("Your password must have a minimum size of 12 and have at least 1 letter, 1 capital letter, 1 number and 1 special character");
    }
}
