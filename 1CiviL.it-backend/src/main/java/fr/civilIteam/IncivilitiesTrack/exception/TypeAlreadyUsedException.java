package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Type already used")
public class TypeAlreadyUsedException extends RuntimeException {
    public TypeAlreadyUsedException() {
        super("Type already used");
    }
    public TypeAlreadyUsedException(String message) {
        super(message);
    }
}
