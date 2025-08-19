package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationRequest;
import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationResponse;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.dto.Users.PasswordTokenRequest;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserCreateDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserUpdateDTO;

import java.util.UUID;

public interface IUserService {
    ResponseUser getSingleUser(UUID uuid);
    public UserDTO getUserById(Long id);
    public UserDTO createUser(UserCreateDTO userCreateDTO);
    public UserDTO updateUser(UUID uuidUser,UUID operatorUUID , UserUpdateDTO updateObjectDTO);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public Boolean changeForgottenPassword (String token, PasswordTokenRequest request);
}
