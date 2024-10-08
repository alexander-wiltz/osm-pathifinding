package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.entities.Way;
import de.hskl.itanalyst.alwi.services.WayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class WayController extends BaseController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WayService wayService;

    @Operation(summary = "Get all ways from database.")
    @ApiResponse(responseCode = "200", description = "Found ways.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WayDTO>> getAllWays() {
        List<Way> ways = wayService.findAllWays();
        return ResponseEntity.ok().body(ways.stream().map(this::convertToDto).toList());
    }

    @Operation(summary = "Get way by id from database.")
    @ApiResponse(responseCode = "200", description = "Way found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WayDTO> getWayById(@PathVariable Long id) {
        Optional<Way> way = wayService.findWayById(id);
        Optional<WayDTO> wayDTO = way.map(this::convertToDto);
        return wayDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add way.")
    @ApiResponse(responseCode = "201", description = "Way added successfully.")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WayDTO> addWay(@Valid @RequestBody WayDTO wayDTO) {
        Way way = convertToEntity(wayDTO);
        Way waySaved = wayService.saveWay(way);
        return ResponseEntity.ok().body(convertToDto(waySaved));
    }

    private WayDTO convertToDto(Way way) {
        return modelMapper.map(way, WayDTO.class);
    }

    private Way convertToEntity(WayDTO wayDTO) {
        return modelMapper.map(wayDTO, Way.class);
    }
}
