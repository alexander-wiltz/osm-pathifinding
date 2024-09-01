package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.services.StreetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/streets")
public class StreetController {

    @Autowired
    private StreetService streetService;

    @PostMapping
    public ResponseEntity<StreetDTO> addStreet(@Valid @RequestBody StreetDTO street) {
        StreetDTO streetDTO = streetService.saveStreet(street);
        return ResponseEntity.ok().body(streetDTO);
    }

    @GetMapping
    public ResponseEntity<List<StreetDTO>> getAllStreets() {
        List<StreetDTO> streets = streetService.findAllStreets();
        return ResponseEntity.ok().body(streets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreetDTO> getStreetById(@PathVariable Long id) {
        Optional<StreetDTO> street = streetService.findStreetById(id);
        return street.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
