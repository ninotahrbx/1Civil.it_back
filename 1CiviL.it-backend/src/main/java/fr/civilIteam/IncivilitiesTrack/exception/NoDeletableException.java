package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "Not deletable ")

public class NoDeletableException extends RuntimeException {
    public NoDeletableException(String message) {
        super(message);
    }
    public NoDeletableException (){super("No deletable ");}
}
