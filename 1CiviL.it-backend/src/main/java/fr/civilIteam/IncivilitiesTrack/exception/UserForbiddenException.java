package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User Forbidden")
public class UserForbiddenException extends RuntimeException {
  public UserForbiddenException() {
    super("User Forbidden");
  }
    public UserForbiddenException(String message) {
        super(message);
    }
}
