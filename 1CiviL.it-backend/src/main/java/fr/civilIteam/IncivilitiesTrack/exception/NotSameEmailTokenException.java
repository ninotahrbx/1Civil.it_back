package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email token is not the same as user Email")
public class NotSameEmailTokenException extends RuntimeException {
    public NotSameEmailTokenException(String message) {
        super(message);
    }
    public NotSameEmailTokenException() {
        super("Email token is not the same as user Email");
    }
}
