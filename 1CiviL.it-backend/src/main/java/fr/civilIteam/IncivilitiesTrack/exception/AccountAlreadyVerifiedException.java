package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Account already validate")
public class AccountAlreadyVerifiedException extends RuntimeException {

    public AccountAlreadyVerifiedException(String message) {

        super(message);
    }
}
