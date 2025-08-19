package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;
import fr.civilIteam.IncivilitiesTrack.exception.NoDeletableException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.RoleNotFoundException;
import fr.civilIteam.IncivilitiesTrack.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
 class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleServiceImpl roleServiceImpl;

    @BeforeEach
    void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
   public void getAll_shouldBeOK()
    {
        List<ResponseRole> roles =new ArrayList<>();
        roles.add(new ResponseRole(UUID.randomUUID(),"utilisateur"));
        roles.add(new ResponseRole(UUID.randomUUID(),"admin"));


        when (roleServiceImpl.getAll()).thenReturn(roles);

        ResponseEntity<List<ResponseRole>>response = roleController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2,response.getBody().size());
    }

    @Test
     void getByUuid_ShouldBeOK()
    {
        UUID uuid =UUID.randomUUID();
        ResponseRole data =new ResponseRole(uuid,"Test");

        when (roleServiceImpl.getByUUID(uuid)).thenReturn(data);

        ResponseEntity<ResponseRole> response =roleController.getByUuid(uuid);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(uuid,response.getBody().uuid());
        assertEquals("Test",response.getBody().name());
    }

    @Test
    void getByUuid_ShouldBeNotFound()
    {
        UUID uuid =UUID.randomUUID();

        when (roleServiceImpl.getByUUID(uuid)).thenThrow(RoleNotFoundException.class);

        assertThrows(RoleNotFoundException.class,()->roleController.getByUuid(uuid));


    }

@Test
    void addNew_shouldBeCreated()
    {
        UUID uuid =UUID.randomUUID();
        ResponseRole data =new ResponseRole(uuid,"Test");
        RequestName body = new RequestName("Test");

        when (roleServiceImpl.addNew(Mockito.any(RequestName.class))).thenReturn(data);

        ResponseEntity<ResponseRole>response =roleController.addnew(body);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test",response.getBody().name());
        assertEquals(uuid,response.getBody().uuid());

    }
@Test
void AddNew_souldBeAlreadyExist()
    {
        UUID uuid = UUID.randomUUID();
        RequestName body= new RequestName("Test");

        when (roleServiceImpl.addNew(Mockito.any(RequestName.class))).thenThrow(RoleAlreadyExistException.class);

        assertThrows(RoleAlreadyExistException.class,()->roleController.addnew(body));

    }


    @Test
    void update_shouldBeOK() {
            UUID uuid = UUID.randomUUID();
            RequestName body = new RequestName("Test");
            ResponseRole data = new ResponseRole(uuid,"Test");

            when (roleServiceImpl.update(uuid,body)).thenReturn(data);

            ResponseEntity<ResponseRole> response =roleController.update(uuid,body);

            assertNotNull(response);
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Test",response.getBody().name());

    }

    @Test
    void update_shouldBeAlreadyExist()
    {
        UUID uuid =UUID.randomUUID();
        RequestName body = new RequestName("Test");

        when(roleServiceImpl.update(uuid,body)).thenThrow(RoleAlreadyExistException.class);

        assertThrows(RoleAlreadyExistException.class,()->roleController.update(uuid,body));

    }

    @Test
    void update_shouldNotFound()
    {
        UUID uuid = UUID.randomUUID();
        RequestName body = new RequestName("Test");

        when(roleServiceImpl.update(uuid,body)).thenThrow(RoleNotFoundException.class);

        assertThrows(RoleNotFoundException.class,()->roleController.update(uuid,body));

    }
    @Test
    void delete_shouldbeOK (){

        UUID uuid = UUID.randomUUID();
        String data = "Role effacé ";

        when (roleServiceImpl.delete(uuid)).thenReturn(true);

       ResponseEntity<String >response = roleController.delete(uuid);

       assertNotNull(response);
       assertNotNull(response.getBody());
       assertEquals(data ,response.getBody());
       assertEquals(HttpStatus.OK,response.getStatusCode());


    }

    @Test
    void Delete_shouldBeNotDeletable()
    {
        UUID uuid =UUID.randomUUID();

        when(roleServiceImpl.delete(uuid)).thenThrow(NoDeletableException.class);

        assertThrows(NoDeletableException.class,()->roleController.delete(uuid));

    }

    @Test
    void Delete_shouldBeNotFound()
    {
        UUID uuid= UUID.randomUUID();

        when (roleServiceImpl.delete(uuid)).thenThrow(RoleNotFoundException.class);

        assertThrows(RoleNotFoundException.class,()->roleController.delete(uuid));

    }
}
