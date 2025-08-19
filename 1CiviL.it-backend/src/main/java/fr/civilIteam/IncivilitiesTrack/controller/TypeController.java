package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.ITypeMapper;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.service.ITypeService;
import fr.civilIteam.IncivilitiesTrack.service.impl.TypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/types")
@RequiredArgsConstructor
public class TypeController {

    private final ITypeService typeService;

    @PostMapping("/add")
    public ResponseEntity<ResponseType> addType(@RequestBody RequestType requestType) {
        Type type = typeService.addType(requestType);
        ResponseType responseType = ITypeMapper.INSTANCE.typeToResponseType(type);
        return ResponseEntity.ok(responseType);
    }

    @PatchMapping("/update/{uuid}")
    public ResponseEntity<ResponseType> updateType(@PathVariable UUID uuid, @RequestBody RequestType requestType) {
        Type updatedType = typeService.updateType(uuid, requestType);
        ResponseType responseType = ITypeMapper.INSTANCE.typeToResponseType(updatedType);
        return ResponseEntity.ok(responseType);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> delete(@PathVariable UUID uuid) {
        typeService.deleteType(uuid);
        return ResponseEntity.ok("Type deleted");
    }

    @GetMapping
    public ResponseEntity<List<ResponseType>> getAll() {
        return ResponseEntity.ok(typeService.getAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseType> getSingleType(@PathVariable UUID uuid) {
        return ResponseEntity.ok(typeService.getSingleType(uuid));
    }
}
