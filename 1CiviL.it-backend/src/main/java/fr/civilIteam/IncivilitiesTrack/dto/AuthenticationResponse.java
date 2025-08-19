package fr.civilIteam.IncivilitiesTrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.civilIteam.IncivilitiesTrack.dto.Users.LoginUserDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponse(
     @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiY2xpZW50In1dLCJzdWIiOiJjbGllbnRAZGFsdG9ucy5mciIsImlhdCI6MTczOTM1MzY0MywiZXhwIjoxNzM5NDQwMDQzfQ.RtSHJwq_PKbuigJ4ji7xWdgHdGmkym1MBS2coh-Wu8M")
     @JsonProperty("access_token")
     String accessToken,
    LoginUserDTO user
) {
}
