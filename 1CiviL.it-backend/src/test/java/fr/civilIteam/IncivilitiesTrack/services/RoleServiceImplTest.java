package fr.civilIteam.IncivilitiesTrack.services;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;
import fr.civilIteam.IncivilitiesTrack.exception.NoDeletableException;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedFieldException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import fr.civilIteam.IncivilitiesTrack.models.User;
import fr.civilIteam.IncivilitiesTrack.repository.IRoleRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
 class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Mock
    private IRoleRepository roleRepository;
    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);


    }

    @Test
    void getAll_shouldBeOk(){
        List<Role> roles =new ArrayList<>();
        roles.add(new Role(1L, UUID.randomUUID(),"utilisateur",null));
        roles.add(new Role(2L, UUID.randomUUID(),"admin",null));

        when (roleRepository.findAll()).thenReturn(roles);
        List<ResponseRole> response = roleServiceImpl.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2,response.size());

    }

    @Test
    void getByUuid_shouldBeOK()
    {
        UUID uuid =UUID.randomUUID();
        Role role =new Role(1L, uuid,"utilisateur",null);

        when (roleRepository.findByUuid(uuid)).thenReturn(Optional.of(role));

        ResponseRole responseRole= roleServiceImpl.getByUUID(uuid);

        assertNotNull(responseRole);
        assertEquals(uuid,responseRole.uuid());
        assertEquals("utilisateur",responseRole.name());

    }

    @Test
    void getByUuid_shouldBeNotFound()
    {

        UUID uuid =UUID.randomUUID();

        when (roleRepository.findByUuid(Mockito.any(UUID.class))).thenThrow(RoleNotFoundException.class);

        assertThrows(RoleNotFoundException.class,()-> roleServiceImpl.getByUUID(uuid));
    }

    @Test
    void addNew_shouldBeOk()
    {
        String bodyname ="superAdmin";
        RequestName body =new RequestName(bodyname);
        Role role = new Role(3L, UUID.randomUUID(),bodyname,null);

        when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

        ResponseRole response = roleServiceImpl.addNew(body);

        assertNotNull(response);
        assertEquals(bodyname,response.name());
        assertNotNull(response.uuid());


    }

    @Test
    void addNew_shouldBeAlreadyExist()
    {
        RequestName body = new RequestName("Test");

        when (roleRepository.save(Mockito.any(Role.class))).thenThrow(RoleAlreadyExistException.class);

        assertThrows(RoleAlreadyExistException.class,()-> roleServiceImpl.addNew(body));

    }

    @Test
    void update_shouldBeOk() {
        UUID uuid = UUID.randomUUID();
        String reqName = "TESTABLE";
        RequestName body = new RequestName(reqName);
        Role data = new Role(1L,uuid,"TEST",new ArrayList<>());
        Role dataModified = new Role(1L,uuid,reqName,new ArrayList<>());

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(roleRepository.save(Mockito.any(Role.class))).thenReturn(dataModified);

        ResponseRole response = roleServiceImpl.update(uuid,body);

        assertNotNull(response);
        assertEquals(reqName,response.name());
        assertEquals(uuid,response.uuid());
    }

    @Test
    void update_shouldBeNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestName body = new RequestName("TESTABLE");

        when(roleRepository.findByUuid(uuid)).thenThrow(RoleNotFoundException.class);

        assertThrows(RoleNotFoundException.class,()-> roleServiceImpl.update(uuid,body));
    }

 @Test
    void update_shouldBeAlreadyExist() {
        UUID uuid = UUID.randomUUID();
       RequestName body = new RequestName("TESTABLE");
        Role data = new Role(1L,uuid,"TEST",new ArrayList<>());
        Role dataModified = new Role(1L,uuid,"TESTABLE",new ArrayList<>());

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
       when(roleRepository.save(Mockito.any(Role.class))).thenReturn(dataModified);
       when(roleRepository.findByNameIgnoreCaseAndUuidNot("TESTABLE",uuid)).thenThrow(RoleAlreadyExistException.class);

        assertThrows(RoleAlreadyExistException.class,()-> roleServiceImpl.update(uuid, body));
   }

    @Test
    void update_shouldBeNotModified() {
        UUID uuid = UUID.randomUUID();
        RequestName body = new RequestName("TEST");
        Role data = new Role(1L,uuid,"TEST",new ArrayList<>());
        Role dataModified = new Role(1L,uuid,"TEST",new ArrayList<>());

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(roleRepository.save(Mockito.any(Role.class))).thenReturn(dataModified);

        assertThrows(NotModifiedFieldException.class,()-> roleServiceImpl.update(uuid,body));
    }

    @Test
    void delete_shouldBeOk() {
        UUID uuid = UUID.randomUUID();
        Role data = new Role(1L,uuid,"TEST",new ArrayList<>());

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(userRepository.findByRole_Uuid(uuid)).thenReturn(new ArrayList<>());

       boolean response = roleServiceImpl.delete(uuid);

        assertEquals(true,response);
    }

    @Test
    void delete_shouldBeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(roleRepository.findByUuid(uuid)).thenThrow(RoleNotFoundException.class);
        when(userRepository.findByRole_Uuid(uuid)).thenReturn(new ArrayList<>());

        assertThrows(RoleNotFoundException.class,()-> roleServiceImpl.delete(uuid));
    }

    @Test
    void delete_shouldBeRoleAlreadyUsed() {
        UUID uuid = UUID.randomUUID();
        Role data = new Role(1L,uuid,"TEST",new ArrayList<>());
        List<User> users = new ArrayList<>();
        users.add(new User());

        when(roleRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(userRepository.findByRole_Uuid(uuid)).thenReturn(users);

        assertThrows(NoDeletableException.class,()-> roleServiceImpl.delete(uuid));
    }

}
