package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseUser;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserProcessDTO;
import fr.civilIteam.IncivilitiesTrack.models.Report;
import fr.civilIteam.IncivilitiesTrack.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IProcessMapper {

        IProcessMapper INSTANCE = Mappers.getMapper(IProcessMapper.class);
        ProcessDTO reportToProcessDto(Report report);
        Report ProcessDTOTReport (ProcessDTO processDTO);

        default UserProcessDTO UserToUserProcessDTO (User user)
        {return Mappers.getMapper(IUserMapper.class).UserToUserProcessDTO(user);}

}
