-- NODES
CREATE TABLE factory_node
(
    id         BIGSERIAL PRIMARY KEY,
    label      VARCHAR(16) UNIQUE NOT NULL,    -- z.B. "D-11"
    col_letter CHAR(1)            NOT NULL,    -- 'A'..'Z'
    row_odd    INTEGER            NOT NULL,    -- 1,3,5,... (VALIDATION!)
    x_m        DOUBLE PRECISION   NOT NULL,    -- Meter in lokalem Koordinatensystem
    y_m        DOUBLE PRECISION   NOT NULL,
    floor      VARCHAR(32) DEFAULT 'EG',
    zone       VARCHAR(64),                    -- optional: Sicherheits-/Produktionszone
    node_type  VARCHAR(32) DEFAULT 'CROSSING', -- CROSSING, LOADING, ENTRY, EXIT, …
    is_blocked BOOLEAN     DEFAULT FALSE,      -- temporär gesperrt (Bau, Störung)
    metadata   JSONB       DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE UNIQUE INDEX ux_factory_node_grid ON factory_node (col_letter, row_odd);

-- EDGES (Kanten)
CREATE TABLE factory_edge
(
    id            BIGSERIAL PRIMARY KEY,
    from_node_id  BIGINT NOT NULL REFERENCES factory_node (id) ON DELETE CASCADE,
    to_node_id    BIGINT NOT NULL REFERENCES factory_node (id) ON DELETE CASCADE,
    bidirectional BOOLEAN     DEFAULT TRUE,
    length_m      DOUBLE PRECISION,          -- optional: vorgespeichert; sonst aus (x,y) berechnen
    speed_mps     DOUBLE PRECISION,          -- optionale Kante-spezifische Geschw. (überschreibt Standard)
    allowed_modes VARCHAR(64) DEFAULT 'ANY', -- ANY, AGV, FORKLIFT, PEDESTRIAN, …
    width_m       DOUBLE PRECISION,          -- verfügbare Breite
    is_blocked    BOOLEAN     DEFAULT FALSE, -- temporär gesperrt
    cost_override DOUBLE PRECISION,          -- wenn gesetzt, nutze das als Kantengewicht
    metadata      JSONB       DEFAULT '{}'::jsonb
);

CREATE INDEX ix_edge_from ON factory_edge (from_node_id);
CREATE INDEX ix_edge_to ON factory_edge (to_node_id);
