package fr.civilIteam.IncivilitiesTrack.models;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private HttpStatus statusString = HttpStatus.FORBIDDEN;
    private int statusCode = 403;
    private List<String> errors = new ArrayList<>();
}