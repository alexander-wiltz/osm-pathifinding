package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.services.StreetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name = "Streets and Addresses")
@RequestMapping("/streets")
public class StreetController extends BaseController {

    @Autowired
    private StreetService streetService;

    @Operation(summary = "All streets from database.")
    @ApiResponse(responseCode = "200", description = "Found streets.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StreetDTO>> getAllStreets() {
        List<StreetDTO> streets = streetService.findAllStreets();
        return ResponseEntity.ok().body(streets);
    }

    @Operation(summary = "Get street by id from database.")
    @ApiResponse(responseCode = "200", description = "Street found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreetDTO> getStreetById(@PathVariable Long id) {
        Optional<StreetDTO> street = streetService.findStreetById(id);
        return street.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get street by name from database.")
    @ApiResponse(responseCode = "200", description = "Address found.")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StreetDTO>> getStreetByAddress(@RequestParam(name = "name") String name, @RequestParam(name = "number", required = false) String housenumber) {
        List<StreetDTO> streets = streetService.findByStreet(name);
        if (housenumber == null || housenumber.isEmpty()) {
            return ResponseEntity.ok().body(streets);
        } else {
            List<StreetDTO> buildings = streets.stream().filter(s -> housenumber.equals(s.getHousenumber())).findFirst().stream().toList();
            return ResponseEntity.ok().body(buildings);
        }
    }

    @Operation(summary = "Add street.")
    @ApiResponse(responseCode = "201", description = "Street added successfully.")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreetDTO> addStreet(@Valid @RequestBody StreetDTO street) {
        StreetDTO streetDTO = streetService.saveStreet(street);
        return ResponseEntity.ok().body(streetDTO);
    }
}
