package de.hskl.itanalyst.alwi.dto;

import de.hskl.itanalyst.alwi.utilities.EdgeStatus;

public class FactoryEdgeDTO {
    public Long id;

    public Long fromId;
    public Long toId;
    public String fromCode;
    public String toCode;

    public boolean bidirectional;
    public boolean blocked;
    public String allowedModes; // "ANY,AGV"
    public Double speedMps;     // optional (null → Default im Routing)
    public Double lengthM;      // optional (null → aus x/y)
    public Double costOverrideSec; // optional

    // Berechnete/effektive Werte (read-only):
    public double effectiveLengthM;
    public double effectiveSpeedMps;
    public double effectiveCostSec;

    public EdgeStatus status;
}
