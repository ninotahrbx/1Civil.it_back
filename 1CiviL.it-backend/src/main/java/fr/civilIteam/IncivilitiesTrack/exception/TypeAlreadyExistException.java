package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Type already exist")
public class TypeAlreadyExistException extends RuntimeException {
    public TypeAlreadyExistException() {
        super("Type already exist");
    }
    public TypeAlreadyExistException(String message) {
        super(message);
    }
}
