package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.exceptions.WayNotFoundException;
import de.hskl.itanalyst.alwi.services.WayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name = "Way-Objects: Buildings, Streets and others")
@RequestMapping("/ways")
public class WayController {

    @Autowired
    private WayService wayService;

    @Operation(summary = "Get all ways from database.")
    @ApiResponse(responseCode = "200", description = "Found ways.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WayDTO>> getAllWays() {
        List<WayDTO> wayDTOs = wayService.findAllWays();
        return ResponseEntity.ok().body(wayDTOs);
    }

    @Operation(summary = "Get way by id from database.")
    @ApiResponse(responseCode = "200", description = "Way found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WayDTO> getWayById(@PathVariable Long id) throws WayNotFoundException {
        Optional<WayDTO> wayDTO = wayService.findWayById(id);
        return wayDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
