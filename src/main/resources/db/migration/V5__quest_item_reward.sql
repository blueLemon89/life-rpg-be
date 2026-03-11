ALTER TABLE quests
    ADD COLUMN IF NOT EXISTS reward_item_id UUID,
    ADD COLUMN IF NOT EXISTS reward_item_quantity INT DEFAULT 1;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_quests_reward_item'
          AND table_name = 'quests'
    ) THEN
        ALTER TABLE quests
            ADD CONSTRAINT fk_quests_reward_item
                FOREIGN KEY (reward_item_id) REFERENCES items(id);
    END IF;
END $$;
