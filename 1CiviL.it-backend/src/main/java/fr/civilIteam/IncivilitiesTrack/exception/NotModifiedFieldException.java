package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_MODIFIED,reason = "Not modified field ")
public class NotModifiedFieldException extends RuntimeException {
    public NotModifiedFieldException(String message) {
        super(message);
    }
    public NotModifiedFieldException() {super ("Not modified field");}
}
