package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.services.WayService;
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
@RequestMapping("api/ways")
public class WayController {

    @Autowired
    private WayService wayService;

    @PostMapping
    public ResponseEntity<WayDTO> addWay(@Valid @RequestBody WayDTO way) {
        WayDTO wayDTO = wayService.saveWay(way);
        return ResponseEntity.ok().body(wayDTO);
    }

    @GetMapping
    public ResponseEntity<List<WayDTO>> getAllWays() {
        List<WayDTO> ways = wayService.findAllWays();
        return ResponseEntity.ok().body(ways);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WayDTO> getWayById(@PathVariable Long id) {
        Optional<WayDTO> way = wayService.findWayById(id);
        return way.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
