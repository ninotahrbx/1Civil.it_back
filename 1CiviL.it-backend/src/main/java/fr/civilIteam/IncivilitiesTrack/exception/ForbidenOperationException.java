package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbiden operation detected")
public class ForbidenOperationException extends RuntimeException {
    public ForbidenOperationException(String message) {
        super(message);
            }
    public ForbidenOperationException(){ super("Forbiden operation detected");}

}
