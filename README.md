# Wegfinder
### Ein kürzester Weg Algorithmus unter Verwendung von OpenStreetMap

##### Bachelorthesis - Alexander Wiltz

Eine JAVA-Implementierung des A*-Algorithmus

### Ablauf
Eine Integration von OSM-Daten, die in einem bestimmten XML-Format vorliegen, werden diese, sofern kein Eintrag vorliegt,
von einem entsprechendem Datei-Handler in ein definiertes Objekt migriert.
Mit diesem Objekt wird mittels Mapping der Dateiinhalt an eine Datenbank in einem Docker-Container abgelegt.

Diese Datenbank bildet die Basis des gesamten Webservices.
Kommt eine Anfrage über einen REST-Endpunkt, so wird im Bereich der Geschäftslogik, über die Nodes ein kürzester Weg mit Hilfe des implementierten A*-Algorithmus berechnet.
Wenn die Berechnung erfolgreich war, wird ein GeoJSON Objekt erstellt und über den REST-Punkt zurückgegeben.

Eine einfache FrontEnd-Integration des Frameworks: Leaflet, bekommt dieses GeoJSON-Objekt und rendert diesen berechneten Weg.

### Start und Ziel
Es muss unterschieden werden, ob es sich um eine genaue Adresse handelt oder lediglich eine Straße.
Der Weg, außer der Start und Zielstraße, darf nur die darauf liegenden Punkte berücksichtigen.
Start und Ziel müssen die dort vorhandenen Gebäude berücksichtigen aufgrund der Ankunftsgenauigkeit.

Nimmt man immer die Gebäude an, kann es passieren, dass aneinanderliegende Gebäude als lose Punkte betrachtet werden und durchschritten werden,
obwohl dies physisch nicht möglich ist.
Gebäude bestehen in der Regel aus vier Datenpunkten, bei denen die Grundstücke als Vierecke dargestellt werden.

Bei den Daten gibt es zur Unterscheidung ein Attribut: Gebäude

### Kosten (Straßentypen)
Der A+-Algorithmus berücksichtigt beim Ermitteln des günstigsten Weges die Kosten einer Option.
Die Kosten setzen sich aus einer möglichen Geschwindigkeit zusammen und der Wegstrecke.
Also die Zeit die benötigt wird, um diese Option zu nutzen.

- Fußwege sind langsamer als Feldwege
- Feldwege sind langsamer als Innerorts
- Innerorts ist langsamer als Landstraßen
- Landstraßen sind langsamer als Autobahnen

## Neue Version für Fabriken

### Anforderungen
Die Anforderung ist, dass eine Fabrikhalle Koordinaten hat, die sich folgendermaßen ergeben: 
Von unten nach oben sind die Pfosten mit Buchstaben gekennzeichnet und von links nach rechts mit Zahlen. Die Zahlen sind allerdings immer nur ungerade!

Im Grunde müssen, also die Nodes der Straßenkreuzungen durch die Nodes der Fabrikwege ersetzt werden und die Straßen sind die Verbindungen zwischen den Nodes. 
Es wird ein sinnvolles Datenmodell benötigt, um die Geo-Nodes in der Fabrik erfassen zu können, um diese später auch in einer UI hinzufügen oder entfernen zu können, als auch die Verbindungen zwischen den Nodes. 

### Was ändert sich?
| Baustein                 | Heute (OSM/Outdoor)      | Morgen (Halle/Indoor)                                       |
| ------------------------ | ------------------------ | ----------------------------------------------------------- |
| Node-Typ                 | `NodeDTO` (lat/lon)      | `FactoryNode` (x,y in m, `label`, `col_letter`, `row_odd`)  |
| Interface                | `INode#getId()`          | **identisch**                                               |
| Graph                    | `Graph<T extends INode>` | **identisch** (nur Aufbau anders)                           |
| Scorer/Heuristik         | `HaversineFormula` (km)  | `PlanarFactoryScorer` (Meter → Sekunden via `/ maxSpeed`)   |
| Kanten                   | Aus OSM-Straßen/Gebäude  | `FactoryEdge` (bidirectional, speed, modes, width, blocked) |
| Kosten pro Kante         | Distanz / implizit       | **Zeit = Länge / Speed** (oder `cost_override`)             |
| Blockierungen            | Gebäude/Polygone         | `is_blocked` auf Node/Edge                                  |
| Start/Ziel               | Adresse vs. Straße       | `Label` (z. B. `D-11`) vs. nur Spalte/Zeile                 |
| GeoJSON                  | WGS84/Map                | Lokal/`L.CRS.Simple` (oder transformiert)                   |
| A\*-Kern (`RouteFinder`) | unverändert              | **unverändert**                                             |

### FactoryNode-Entity
```java
@Entity
@Table(name = "factory_node", uniqueConstraints = @UniqueConstraint(columnNames = {"col_letter","row_odd"}))
public class FactoryNode implements INode {
  @Id @GeneratedValue private Long id;

  @Column(nullable=false, length=16) private String label;      // "D-11"
  @Column(name="col_letter", nullable=false, length=1) private String colLetter; // "D"
  @Column(name="row_odd",   nullable=false) private int rowOdd; // 11
  @Column(name="x_m",       nullable=false) private double x;   // Meter
  @Column(name="y_m",       nullable=false) private double y;
  private String floor = "EG";
  private String zone;
  private String nodeType = "CROSSING";
  private boolean isBlocked = false;

  // INode:
  @Override public Long getId() { return id; }

  // Praktische Getter:
  public String getLabel() { return label; } // "D-11"
  public double getX() { return x; }
  public double getY() { return y; }
}
```

### FactoryEdge-Entity
```java
@Entity
@Table(name = "factory_edge")
public class FactoryEdge {
  @Id @GeneratedValue private Long id;

  @ManyToOne(optional=false) private FactoryNode fromNode;
  @ManyToOne(optional=false) private FactoryNode toNode;

  private boolean bidirectional = true;
  private Double lengthM;      // optional (falls nicht gesetzt -> aus (x,y) berechnen)
  private Double speedMps;     // optional (falls null -> default pro Modus/Zonenregel)
  private String allowedModes = "ANY";
  private Double widthM;
  private boolean isBlocked = false;
  private Double costOverride;
}
```

### Kostenmodell - Neuer Scorer
Vorher wurde eine Haversine-Formel für GeoKoordinaten eingesetzt. Aufgrund der x/y-Koordinaten wird ein PlanarFactoryScorer eingesetzt
```java
public class PlanarFactoryScorer implements IScorer<FactoryNode> {

  private final double maxSpeedMps; // z.B. 2.0 m/s (AGV/Stapler schnellste erlaubte Kante)

  public PlanarFactoryScorer(double maxSpeedMps) {
    this.maxSpeedMps = maxSpeedMps;
  }

  // Heuristik: LUFTLINIE / MAX_SPEED  → zulässig (nicht überschätzend)
  @Override
  public double computeDistance(FactoryNode from, FactoryNode to) {
    double dx = from.getX() - to.getX();
    double dy = from.getY() - to.getY();
    double euclidMeters = Math.sqrt(dx*dx + dy*dy);
    return euclidMeters / maxSpeedMps; // Sekunden
  }
  
  @Override
  public FactoryNode findClosestNode(FactoryNode target, List<FactoryNode> candidates) {
    FactoryNode best = null;
    double bestD = Double.MAX_VALUE;
    for (FactoryNode n : candidates) {
      double d = Math.hypot(n.getX()-target.getX(), n.getY()-target.getY());
      if (d < bestD) { bestD = d; best = n; }
    }
    return best;
  }
}
```

##### Vergleich:
Haversine rechnet auf der Erdkugel und liefert Distanzen in km – indoor falsch und numerisch unnötig teuer.
PlanarFactoryScorer ist euklidisch in Metern und teilt durch maxSpeed → Heuristik in Sekunden wie di Kantengewichte (Zeit). Damit bleibt A* optimal.

### Hinweise & Design-Entscheidungen
- Subindex-Position: Bei F089.4 platzieren wir den Node im Mittelpunkt des gewählten Teilrechtecks (präzise, UI-freundlich). Ohne Subindex landet der Node exakt auf der Ecke (Basis-Koordinate).
- Validierung: Reihen sind immer ungerade. Parser wirft bei Verstoß eine verständliche Exception.
- Skalierung/Offset: GridMapper(0,0) setzt A01 = (0,0). Falls die Halle relativ verschoben ist, können originX/Y gesetzt werden.
- Kostenmodell: Kantenkosten sind Zeit in Sekunden (Länge / m/s oder Override). Heuristik ist ebenfalls Zeit (zulässig, da durch Max-Speed geteilt).
- Modi/Sperren: allowedModes und blocked wirken beim Graph-Build. So kannst du zur Laufzeit Bereiche sperren, ohne die Topologie zu ändern.
- Namen: FactoryNode.name ist frei (z. B. „Anfahrplatz 7“). Der maschinenlesbare Code bleibt in code.
- Leaflet: Mit L.CRS.Simple kannst du die GeoJSON-Line direkt rendern (Einheiten = Meter).

### Stolpersteine & Tipps
- Zulässige Heuristik: Teile immer durch die höchste erlaubte Geschwindigkeit → A* bleibt optimal.
- Richtungswechsel/Abbiegen: Falls relevant, füge turn penalties an Knoten ein (kleiner Aufschlag auf routeScore).
- Sperren im Betrieb: Nutze is_blocked (Node/Edge) → keine Topologie-Änderungen nötig, nur Flags.
- Zonen-Defaults: Halte eine Tabelle/Config mode × zone → default_speed_mps.
- UI-Komfort: Auto-Generator fürs Raster + Batch-Verbindungen spart viel Klickarbeit.

### ToDo's
- Mapper D-11 ↔ (x,y) bei gegebenem Rasterabstand
- GraphBuilder für FactoryNode/FactoryEdge,
- sowie ein Minimal-Controller für CRUD (Nodes/Edges) inkl. Validierungen.

