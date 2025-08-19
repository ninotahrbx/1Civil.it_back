package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IRoleMapper;
import fr.civilIteam.IncivilitiesTrack.exception.NoDeletableException;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedFieldException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import fr.civilIteam.IncivilitiesTrack.repository.IRoleRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    /**
     * Allow to get liste of role
     * @return liste of all role
     */
    public List<ResponseRole> getAll()
    {
        List<ResponseRole> roles = new ArrayList<>();
        for (Role role :roleRepository.findAll())
        {
            roles.add(IRoleMapper.INSTANCE.roleToResponseRole(role));
        }

        return roles;
    }

    /**
     * get a role by UUID
     * @param uuid
     * @return a responseRole object
     * @Error RoleNotFoundException
     */
    public ResponseRole  getByUUID (UUID uuid ) throws RoleNotFoundException
    {
        return IRoleMapper.INSTANCE.roleToResponseRole(roleRepository.findByUuid(uuid ).orElseThrow(()->new RoleNotFoundException()));
    }

    /**
     * get a role by his name
     * @param name
     * @return a responseRole object
     * @Error RoleNotFoundException
     */
    public ResponseRole getByName (String name ) throws RoleNotFoundException
    {
        return IRoleMapper.INSTANCE.roleToResponseRole(roleRepository.findByName(name).orElseThrow(()->new RoleNotFoundException()));
    }

    /**
     * create a new role
     * @param requestName
     * @return a ResponseRole object
     * @Error RoleAlreadyExistsException
     */
    public ResponseRole addNew (RequestName requestName) throws RoleAlreadyExistException
    {
        if(roleRepository.findByNameIgnoreCase(requestName.name()).isPresent())
            throw new RoleAlreadyExistException();

        Role role = new Role();
        role.setName(requestName.name());
        return IRoleMapper.INSTANCE.roleToResponseRole(roleRepository.save(role));

    }

    public ResponseRole update (UUID uuid ,RequestName name ) throws NotModifiedFieldException,RoleNotFoundException,RoleAlreadyExistException
    {
        Role role = roleRepository.findByUuid(uuid ).orElseThrow(()->new RoleNotFoundException());

        if(role.getName().equalsIgnoreCase(name.name()))
            throw new NotModifiedFieldException();

        if(roleRepository.findByNameIgnoreCaseAndUuidNot(name.name(),uuid).isPresent())
            throw new RoleAlreadyExistException();

        role.setName(name.name());
        return IRoleMapper.INSTANCE.roleToResponseRole(roleRepository.save(role));
    }


    public boolean delete (UUID uuid)throws NoDeletableException
    {
        Role role = roleRepository.findByUuid(uuid).orElseThrow(()->new NoDeletableException());

        if(userRepository.findByRole_Uuid(uuid).size()>0)
        {
            throw  new NoDeletableException();
        }

        roleRepository.delete(role);

        return  true;
    }

}
