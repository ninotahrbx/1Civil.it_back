package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IDocumentTypeMapper;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import fr.civilIteam.IncivilitiesTrack.service.IDocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final IDocumentTypeService documentTypeService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDocumentType> addDocumentType(@RequestBody RequestDocumentType requestDocumentType) {
        DocumentType documentType = documentTypeService.addDocumentType(requestDocumentType);
        ResponseDocumentType responseDocumentType = IDocumentTypeMapper.INSTANCE.documentTypeToResponseDocumentType(documentType);
        return ResponseEntity.ok(responseDocumentType);
    }

    @PatchMapping("update/{uuid}")
    public ResponseEntity<ResponseDocumentType> updateDocumentType(@PathVariable UUID uuid, @RequestBody RequestDocumentType requestDocumentType) {
        DocumentType updatedDocumentType = documentTypeService.updateDocumentType(uuid, requestDocumentType);
        ResponseDocumentType responseDocumentType = IDocumentTypeMapper.INSTANCE.documentTypeToResponseDocumentType(updatedDocumentType);
        return ResponseEntity.ok(responseDocumentType);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> delete(@PathVariable UUID uuid) {
        documentTypeService.deleteDocumentType(uuid);
        return  ResponseEntity.ok("Document Type deleted");
    }

    @GetMapping
    public ResponseEntity<List<ResponseDocumentType>> getAll() {
        return ResponseEntity.ok(documentTypeService.getAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDocumentType> getSingleDocumentType(@PathVariable UUID uuid) {
        return ResponseEntity.ok(documentTypeService.getSingleDocumentType(uuid));
    }
}
