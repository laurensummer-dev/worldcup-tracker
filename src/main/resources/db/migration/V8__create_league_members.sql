CREATE TABLE league_members (
    id          BIGSERIAL PRIMARY KEY,
    league_id   BIGINT    NOT NULL REFERENCES leagues(id),
    user_id     BIGINT    NOT NULL REFERENCES users(id),
    joined_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(league_id, user_id)
);