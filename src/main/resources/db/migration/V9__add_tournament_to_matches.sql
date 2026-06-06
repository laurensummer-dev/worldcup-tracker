ALTER TABLE matches
ADD COLUMN tournament_id BIGINT REFERENCES tournaments(id);