package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_MODIFIED, reason = "Not modified")
public class NotModifiedException extends RuntimeException {
    public NotModifiedException(String message) {
        super(message);
    }
}
