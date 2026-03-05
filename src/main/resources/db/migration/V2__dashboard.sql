CREATE TABLE IF NOT EXISTS characters (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    level INT NOT NULL DEFAULT 1,
    xp INT NOT NULL DEFAULT 0,
    gold INT NOT NULL DEFAULT 0,

    hp INT NOT NULL DEFAULT 100,
    max_hp INT NOT NULL DEFAULT 100,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR,

    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skills (
    id UUID PRIMARY KEY,

    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR
);

CREATE TABLE IF NOT EXISTS character_skills (
    id UUID PRIMARY KEY,

    character_id UUID NOT NULL,
    skill_id UUID NOT NULL,

    level INT NOT NULL DEFAULT 1,
    xp INT NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR,

    UNIQUE(character_id, skill_id),

    FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);
CREATE TABLE IF NOT EXISTS quests (
  id UUID PRIMARY KEY,

  title VARCHAR(255) NOT NULL,
    description TEXT,

    difficulty VARCHAR(20) NOT NULL,
    -- EASY
    -- NORMAL
    -- HARD
    -- BOSS

    xp_reward INT NOT NULL,
    gold_reward INT DEFAULT 0,

    skill_id UUID,

    type VARCHAR(50),
    -- DAILY
    -- WEEKLY
    -- CHALLENGE
    -- BOSS

    unlock_level INT DEFAULT 1,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR,

    FOREIGN KEY (skill_id) REFERENCES skills(id)
    );

CREATE TABLE IF NOT EXISTS quest_completions (
     id UUID PRIMARY KEY,

     character_id UUID NOT NULL,
     quest_id UUID NOT NULL,

     xp_earned INT NOT NULL,
     gold_earned INT NOT NULL,

     completed_at TIMESTAMPTZ NOT NULL,

     created_at TIMESTAMPTZ NOT NULL,
     created_by VARCHAR,
     updated_at TIMESTAMPTZ NOT NULL,
     updated_by VARCHAR,

     FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (quest_id) REFERENCES quests(id)
    );

CREATE TABLE IF NOT EXISTS items (
   id UUID PRIMARY KEY,

   code VARCHAR(50) UNIQUE,
   name VARCHAR(100),

   description TEXT,
   type VARCHAR(50),

   xp_boost INT,
   skill_boost INT,

   created_at TIMESTAMPTZ NOT NULL,
   created_by VARCHAR,
   updated_at TIMESTAMPTZ NOT NULL,
   updated_by VARCHAR
);

CREATE TABLE IF NOT EXISTS inventories (
 id UUID PRIMARY KEY,

 character_id UUID NOT NULL,
 item_id UUID NOT NULL,

 quantity INT NOT NULL DEFAULT 1,

 created_at TIMESTAMPTZ NOT NULL,
 created_by VARCHAR,
  updated_at TIMESTAMPTZ NOT NULL,
  updated_by VARCHAR,

  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS rewards (
    id UUID PRIMARY KEY,

    character_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,
    description TEXT,

    unlock_condition VARCHAR(255),

    type VARCHAR(50),

    is_unlocked BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR,

    FOREIGN KEY (character_id) REFERENCES characters(id)
);

CREATE TABLE IF NOT EXISTS character_stats (
     character_id UUID PRIMARY KEY,

     current_streak INT DEFAULT 0,
     best_streak INT DEFAULT 0,

     completed_quests INT DEFAULT 0,
     total_xp INT DEFAULT 0,

     created_at TIMESTAMPTZ NOT NULL,
     created_by VARCHAR,
     updated_at TIMESTAMPTZ NOT NULL,
     updated_by VARCHAR,

     FOREIGN KEY (character_id) REFERENCES characters(id)
);

CREATE TABLE IF NOT EXISTS character_quests (
    id UUID PRIMARY KEY,

    character_id UUID NOT NULL,
    quest_id UUID NOT NULL,

    status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE
    -- COMPLETED
    -- EXPIRED

    assigned_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR,
    updated_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR,

    FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (quest_id) REFERENCES quests(id)
    );
