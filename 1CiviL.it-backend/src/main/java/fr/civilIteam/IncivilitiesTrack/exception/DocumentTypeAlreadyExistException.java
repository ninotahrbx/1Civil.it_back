package fr.civilIteam.IncivilitiesTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Document type already exist")
public class DocumentTypeAlreadyExistException extends RuntimeException {
    public DocumentTypeAlreadyExistException() {
        super("Document type already exist");
    }
    public DocumentTypeAlreadyExistException(String message) {
        super(message);
    }
}
