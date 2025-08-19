package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Report not found")
public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super("Type not found");
    }
    public ReportNotFoundException(String message) {
        super(message);
    }
}
