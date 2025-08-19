package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Document Type not found")
public class DocumentTypeNotFoundException extends RuntimeException {
    public DocumentTypeNotFoundException() {
        super("Document Type not found");
    }
    public DocumentTypeNotFoundException(String message) {
        super(message);
    }
}
