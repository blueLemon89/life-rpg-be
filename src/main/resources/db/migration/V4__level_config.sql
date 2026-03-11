CREATE TABLE IF NOT EXISTS level_config (
    id UUID PRIMARY KEY,
    level INT NOT NULL,
    xp_required INT NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR
    );

INSERT INTO level_config (level, xp_required)
VALUES
    (1, 100),
    (2, 150),
    (3, 220),
    (4, 310),
    (5, 430),
    (6, 580),
    (7, 760),
    (8, 970),
    (9, 1210),
    (10, 1480);