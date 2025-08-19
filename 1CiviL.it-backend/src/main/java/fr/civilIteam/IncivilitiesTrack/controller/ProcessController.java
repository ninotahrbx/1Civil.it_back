package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;
import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.service.impl.ProcessServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessServiceImpl processServiceImpl;

        @GetMapping
        @Secured("admin")
    public ResponseEntity<List<ProcessDTO>> ListProcess()
        {
            return ResponseEntity.ok(processServiceImpl.getAllActive());
        }

        @PutMapping("/{uuid}")
        @Secured("admin")
    public ResponseEntity<String>UpdateStatus(@PathVariable UUID uuid , @RequestBody RequestUUID uuidStatus)
        {
            processServiceImpl.UpdateStatusReport(uuid, uuidStatus.uuid());
           return ResponseEntity.ok("status mis a jour ");
        }
}
