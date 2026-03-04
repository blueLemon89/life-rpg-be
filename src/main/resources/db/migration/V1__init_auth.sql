CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_profile (
    user_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    avatar_type VARCHAR(50) NOT NULL,
    avatar_preset_id VARCHAR(100),
    avatar_image_url TEXT,
    title VARCHAR(100) NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX ux_users_email ON users (email);
