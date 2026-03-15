CREATE TABLE IF NOT EXISTS quest_config (
    id BIGSERIAL PRIMARY KEY,
    level_min INT NOT NULL,
    level_max INT NOT NULL,
    quest_difficult VARCHAR(200) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR
);