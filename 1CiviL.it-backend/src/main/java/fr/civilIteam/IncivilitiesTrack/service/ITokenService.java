package fr.civilIteam.IncivilitiesTrack.service;

public interface ITokenService {

    public String generateToken(String email);
    public String extractEmailFromToken(String token);
    public boolean isTokenValid(String token);
}
