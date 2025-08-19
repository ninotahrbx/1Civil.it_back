package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestStatus;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseStatus;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IStatusMapper;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.ITypeMapper;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.service.IStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    private final IStatusService statusService;

    @PostMapping("/add")
    public ResponseEntity<ResponseStatus> addStatus(@RequestBody RequestStatus requestStatus) {
        Status status = statusService.addStatus(requestStatus);
        ResponseStatus responseStatus = IStatusMapper.INSTANCE.statusToResponseStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseStatus);
    }

   @PatchMapping("/update/{uuid}")
    public ResponseEntity<ResponseStatus> updateStatus(@PathVariable UUID uuid, @RequestBody RequestStatus requestStatus) {
       Status status = statusService.updateStatus(uuid, requestStatus);
       ResponseStatus responseStatus = IStatusMapper.INSTANCE.statusToResponseStatus(status);
       return ResponseEntity.status(HttpStatus.OK).body(responseStatus);
    }

    @GetMapping
    public ResponseEntity<List<ResponseStatus>> getAllStatus() {
        List<ResponseStatus> responses = statusService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseStatus> getStatusByUuid(@PathVariable UUID uuid) {
        ResponseStatus response = statusService.getSingleStatus(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<Void> deleteStatus(@PathVariable UUID uuid) {
        statusService.deleteStatus(uuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
