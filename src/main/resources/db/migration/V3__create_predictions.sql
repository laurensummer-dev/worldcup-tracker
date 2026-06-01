CREATE TABLE predictions (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES users(id),
    match_id        BIGINT       NOT NULL REFERENCES matches(id),
    home_score      INTEGER      NOT NULL,
    away_score      INTEGER      NOT NULL,
    points_awarded  INTEGER      NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, match_id)
);