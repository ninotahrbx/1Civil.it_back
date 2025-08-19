package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Document Type already used")
public class DocumentTypeAlreadyUsedException extends RuntimeException {
    public DocumentTypeAlreadyUsedException() {
        super("Document Type already used");
    }
    public DocumentTypeAlreadyUsedException(String message) {
        super(message);
    }
}
