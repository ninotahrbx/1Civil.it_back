package fr.civilIteam.IncivilitiesTrack.exception.handler;

import fr.civilIteam.IncivilitiesTrack.exception.*;
import fr.civilIteam.IncivilitiesTrack.models.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ErrorMessage responseMessage(HttpStatus status, RuntimeException ex) {
        System.out.println(ex.getMessage());
        ErrorMessage response = new ErrorMessage();
        response.setStatusString(status);
        response.setStatusCode(status.value());
        response.getErrors().add(ex.getMessage());

        return response;
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorMessage> roleNotFound(RoleNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(RoleAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> roleAlreadyExist(RoleAlreadyExistException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(NotModifiedFieldException.class)
    public ResponseEntity<ErrorMessage> notModified(NotModifiedFieldException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_MODIFIED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(NoDeletableException.class)
    public ResponseEntity<ErrorMessage> noDeletable(NoDeletableException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(TypeAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> TypeAlreadyExist(TypeAlreadyExistException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(TypeNotFoundException.class)
    public ResponseEntity<ErrorMessage> TypeNotFound(TypeNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(TypeAlreadyUsedException.class)
    public ResponseEntity<ErrorMessage> TypeAlreadyUsed(TypeAlreadyUsedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(InvalidPasswordRegexException.class)
    public ResponseEntity<ErrorMessage> invalidPasswordRegex(InvalidPasswordRegexException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFound(UserNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(StatusAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> StatusAlreadyExist(StatusAlreadyExistException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<ErrorMessage> StatusNotFound(StatusNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(ForbidenOperationException.class)
    public ResponseEntity<ErrorMessage> forbidenOperation(ForbidenOperationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<ErrorMessage> EmptyField(EmptyFieldException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(InvalidEmailRegexException.class)
    public ResponseEntity<ErrorMessage> InvalidEmail(InvalidEmailRegexException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> InvalidEmail(EmailAlreadyExistException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(StatusAlreadyUsedException.class)
    public ResponseEntity<ErrorMessage> StatusAlreadyUsed(StatusAlreadyUsedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(NotModifiedException.class)
    public ResponseEntity<ErrorMessage> NotModified(NotModifiedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_MODIFIED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(DocumentTypeAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> DocumentTypeAlreadyExist(DocumentTypeAlreadyExistException ex,
            WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(DocumentTypeNotFoundException.class)
    public ResponseEntity<ErrorMessage> DocumentTypeNotFound(DocumentTypeNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(DocumentTypeAlreadyUsedException.class)
    public ResponseEntity<ErrorMessage> DocumentTypeAlreadyUsed(DocumentTypeAlreadyUsedException ex,
            WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<ErrorMessage> UserForbidden(UserForbiddenException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorMessage> TokenExpired(TokenExpiredException ex, WebRequest request) {
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(NotSameEmailTokenException.class)
    public ResponseEntity<ErrorMessage> NotSameEmail(NotSameEmailTokenException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorMessage> ReportNotFound(ReportNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseMessage(status, ex));
    }

    @ExceptionHandler(AccountAlreadyVerifiedException.class)
    public ResponseEntity<String> handleAccountAlreadyVerifiedException(AccountAlreadyVerifiedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error

    }
}
