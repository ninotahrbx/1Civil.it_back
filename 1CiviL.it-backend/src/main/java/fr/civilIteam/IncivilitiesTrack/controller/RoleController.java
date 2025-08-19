package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;
import fr.civilIteam.IncivilitiesTrack.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleServiceImpl;

    @GetMapping
    @Secured({"utilisateur","admin"})
    public ResponseEntity<List<ResponseRole>>getAll(){
        return ResponseEntity.ok(roleServiceImpl.getAll());
    }

    @GetMapping("/{uuid}")
    @Secured({"utilisateur","admin"})
    public ResponseEntity<ResponseRole>getByUuid(@PathVariable UUID uuid )
    {
        return ResponseEntity.ok(roleServiceImpl.getByUUID(uuid));
    }

    @Secured({"admin"})
    @PostMapping("/add")
    public ResponseEntity<ResponseRole>addnew (@RequestBody RequestName name )
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleServiceImpl.addNew(name));
    }

    @Secured({"admin"})
    @PatchMapping ("/update/{uuid}")
    public ResponseEntity<ResponseRole> update (@PathVariable UUID uuid,@RequestBody RequestName name  )
    {
        return ResponseEntity.ok(roleServiceImpl.update(uuid,name));
    }

    @Secured({"admin"})
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> delete (@PathVariable UUID uuid )
    {
        roleServiceImpl.delete(uuid);

        return ResponseEntity.ok("Role effacé ");
    }

}
