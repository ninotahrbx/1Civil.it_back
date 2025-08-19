package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Status already used")
public class StatusAlreadyUsedException extends RuntimeException {
    public StatusAlreadyUsedException() {
        super("Status already used");
    }
    public StatusAlreadyUsedException(String message) {
        super(message);
    }
}
