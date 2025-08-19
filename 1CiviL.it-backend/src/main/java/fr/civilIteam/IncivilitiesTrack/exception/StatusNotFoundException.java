package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Status not found")
public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException() {
        super("Status not found");
    }
    public StatusNotFoundException(String message) {
        super(message);
    }
}
