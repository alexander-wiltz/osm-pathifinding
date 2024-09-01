package de.hskl.itanalyst.alwi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name="Berechnung der angeforderten Wege")
@RequestMapping("/pathfinding")
public class PathfindingController extends BaseController {
}
