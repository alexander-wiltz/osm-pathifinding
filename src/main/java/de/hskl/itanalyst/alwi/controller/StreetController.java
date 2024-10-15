package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.services.StreetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "Streets and Addresses")
@RequestMapping("/streets")
public class StreetController {

    @Autowired
    private StreetService streetService;

    @Operation(summary = "All streets from database.")
    @ApiResponse(responseCode = "200", description = "Found streets.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StreetDTO>> getAllStreets() {
        List<StreetDTO> streetDTOs = streetService.findAllStreets();
        return ResponseEntity.ok().body(streetDTOs);
    }

    @Operation(summary = "Get a list of integrated streets from database.")
    @ApiResponse(responseCode = "200", description = "Found streets.")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StreetDTO>> getListOfAllStreets() {
        List<StreetDTO> streetDTOs = streetService.findListOfAllStreets();
        return ResponseEntity.ok().body(streetDTOs);
    }

    @Operation(summary = "Get street by id from database.")
    @ApiResponse(responseCode = "200", description = "Street found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreetDTO> getStreetById(@PathVariable Long id) throws StreetNotFoundException {
        Optional<StreetDTO> streetDTO = streetService.findStreetById(id);
        return streetDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get street by name and optional house number from database.")
    @ApiResponse(responseCode = "200", description = "Address found.")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StreetDTO>> getStreetByAddress(@RequestParam(name = "name") String name, @RequestParam(name = "number", required = false) String houseNumber) throws StreetNotFoundException {
        List<StreetDTO> streetDTOs = streetService.findByStreet(name);
        if (houseNumber == null || houseNumber.isEmpty()) {
            return ResponseEntity.ok().body(streetDTOs);
        } else {
            List<StreetDTO> buildings = streetDTOs.stream().filter(s -> houseNumber.equals(s.getHouseNumber())).findFirst().stream().toList();
            return ResponseEntity.ok().body(buildings);
        }
    }
}
