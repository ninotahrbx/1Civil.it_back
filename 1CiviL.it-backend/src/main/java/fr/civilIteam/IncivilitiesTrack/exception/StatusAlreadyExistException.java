package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Status already exist")
public class StatusAlreadyExistException extends RuntimeException {
    public StatusAlreadyExistException() {
        super("Status already exist");
    }
    public StatusAlreadyExistException(String message) {
        super(message);
    }
}
