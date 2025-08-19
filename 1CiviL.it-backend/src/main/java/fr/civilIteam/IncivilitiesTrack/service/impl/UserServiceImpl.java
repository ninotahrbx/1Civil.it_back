package fr.civilIteam.IncivilitiesTrack.service.impl;


import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationRequest;
import fr.civilIteam.IncivilitiesTrack.dto.AuthenticationResponse;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.dto.Users.*;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IUserMapper;
import fr.civilIteam.IncivilitiesTrack.exception.*;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IRoleRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.IUserService;
import fr.civilIteam.IncivilitiesTrack.service.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IUserMapper userMapper;
    private final JwtService jwtService;
    private final TokenServiceImpl tokenService;




    public UserDTO getUserById(Long id) {
        User user = (User) userRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.usertoUserDTO(user);


    }

    public UserDTO getByUuid(UUID uuid )
    {
        User user = userRepository.findByUuid(uuid).orElseThrow(()->new UserNotFoundException());

        return userMapper.usertoUserDTO(user);
    }


    public UserDTO createUser(UserCreateDTO userCreateDTO) {


        validateRequiredField(userCreateDTO);

        User user = castUserCreateDTOToUser(userCreateDTO);

        if(userRepository.findByEmail(userCreateDTO.getEmail()).isPresent())
            throw new EmailAlreadyExistException();

        if(user.getRole()==null)
            user.setRole(roleRepository.findByName("utilisateur").orElseThrow(()->new RoleNotFoundException()));
        else
            user.setRole(roleRepository.findByUuid(user.getRole().getUuid()).orElseThrow(()->new RoleNotFoundException()));

        user.setPassword(hashPassword(user.getPassword())); // Hashage du mot de passe
        userRepository.save(user);
        return userMapper.usertoUserDTO(user);
    }

    /**
     * return a user objet from a user DTO OR AdminCreate DTO
     * @param userCreateDTO
     * @return
     */
    private User castUserCreateDTOToUser(UserCreateDTO userCreateDTO)
    {
        User user =null;
        if(userCreateDTO.getClass()==UserCreateDTO.class)
            user = userMapper.toUser(userCreateDTO);
        else if (userCreateDTO.getClass()== AdminCreateDTO.class)
            user=userMapper.adminCreateDTOToUser((AdminCreateDTO) userCreateDTO);

        return user;

    }

    /**
     * Validate the required fields
     * @param userObjectDTO
     */
    private void validateRequiredField(UserObjectDTO userObjectDTO)
    {
        String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$";
        if (!userObjectDTO.getPassword().matches(password_regex)) throw new InvalidPasswordRegexException();

        //verification des champs obligatoires
        if(userObjectDTO.getName()==null || userObjectDTO.getName().isEmpty())
            throw new EmptyFieldException();

        if(userObjectDTO.getFirst_name()==null|| userObjectDTO.getFirst_name().isEmpty())
            throw new EmptyFieldException();

        if(userObjectDTO.getPhone()==null||userObjectDTO.getPhone().isEmpty())
            throw new EmptyFieldException();

        String emailRegex ="^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(!userObjectDTO.getEmail().matches(emailRegex))
            throw new InvalidEmailRegexException();


    }

    public UserDTO updateUser(UUID uuidUser,UUID operatorUUID , UserUpdateDTO updateObjectDTO)throws UserNotFoundException,InvalidPasswordRegexException,ForbidenOperationException,EmptyFieldException {

        //verification que les 2 uuid appartienne a des utilisateur en BDD
        User operator = userRepository.findByUuid(operatorUUID).orElseThrow(()->new UserNotFoundException());


        User user = userRepository.findByUuid(uuidUser)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        //si l'uuid de l'operateur est different de l'id modifier et n'est pas admin : operation interdite
        if (uuidUser!=operator.getUuid())
            if(operator.getRole().getId()!=2)
                throw new ForbidenOperationException();


        validateRequiredField(updateObjectDTO);

        if(userRepository.findByEmailAndUuidNot(updateObjectDTO.getEmail(),uuidUser).isPresent())
            throw new EmailAlreadyExistException();

        user.setRole(roleRepository.findByUuid(user.getRole().getUuid()).orElseThrow(RoleNotFoundException::new));

        userMapper.updateUserFromDTO(updateObjectDTO, user);
        user.setDateModify(new Date());
        userRepository.save(user);
        return userMapper.usertoUserDTO(user);
    }

    private String hashPassword(String password) {
        // Exemple : Implémenter un encodage sécurisé (ex: BCrypt)
        return  passwordEncoder.encode(password);

    }


    /**
     * Allow to log in
     * @param request DTO with parameters
     * @return a Json Web Token
     * @throws UserNotFoundException if the user's email doesn't exist in DB
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException {
        try {
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            );
            User user = userRepository.findByEmail(request.email()).orElseThrow(
                    ()->new UserNotFoundException("Incorrect credentials"));
            authenticationManager.authenticate(authenticationToken);
            final String token = jwtService.generateToken(user);
            AuthenticationResponse response = new AuthenticationResponse(token,IUserMapper.INSTANCE.entityToDTO(user));
            //Set dateConnect
            user.setDateConnect(new Date());
            userRepository.save(user);
            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email/password supplied");
        }
    }

    /**
     * Allow to get a specific user
     * @param uuid UUId of the user
     * @return a single user
     * @throws UserNotFoundException if the user's UUID doesn't exist in DB
     */
    @Override
    public ResponseUser getSingleUser(UUID uuid) throws UserNotFoundException {
        User user = userRepository.findByUuid(uuid).orElseThrow(UserNotFoundException::new);
        return new ResponseUser(user.getUuid(),user.getName(), user.getFirst_name(), user.getEmail(),user.getPhone(),user.getRole().getName());
    }

    /**
     * -validate the token
     * -change password
     * @param token
     * @param request
     * @return
     */
    public Boolean changeForgottenPassword (String token, PasswordTokenRequest request)
    {
        User user = userRepository.findByTokenPassword(token).orElseThrow(()->new UserNotFoundException());

        if(!tokenService.isTokenValid(token))
            throw new TokenExpiredException();


        if(!tokenService.extractEmailFromToken(token).equals(user.getEmail()))
            throw new NotSameEmailTokenException();


        user.setPassword(hashPassword(request.getPassword()));
        user.setTokenPassword(null);

        userRepository.save(user);

        return true;

    }
}
