package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.dto.Users.*;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import fr.civilIteam.IncivilitiesTrack.dto.RequestUser;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    ResponseUser userToResponseUser(User user);
    User requestUserToUser(RequestUser requestUser);

    UserDTO usertoUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "dateCreation", expression = "java(new java.util.Date())")
    @Mapping(target = "dateModify", ignore = true)
    @Mapping(target = "dateConnect", ignore = true)
    @Mapping(target = "role", ignore = true) // Géré dans le service
    User toUser(UserCreateDTO userCreateDTO);

    UserProcessDTO UserToUserProcessDTO(User user);

    @Mapping(target = "dateCreation", expression = "java(new java.util.Date())")
    User adminCreateDTOToUser(AdminCreateDTO adminCreateDTO);

    void updateUserFromDTO(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    @Mapping(source = "first_name",target = "firstname")
    LoginUserDTO entityToDTO(User user);

    default Role requestUuidToEntity(RequestUUID role )
    {  return Mappers.getMapper(IRoleMapper.class).RequestUUIDToRole(role);}


}
