ALTER TABLE user_scores
ADD COLUMN tournament_id BIGINT REFERENCES tournaments(id);

-- Drop the existing unique constraint on user_id since scores are now per user per tournament
ALTER TABLE user_scores
DROP CONSTRAINT user_scores_user_id_key;

-- Add new unique constraint on user_id + tournament_id
ALTER TABLE user_scores
ADD CONSTRAINT user_scores_user_tournament_unique
UNIQUE(user_id, tournament_id);