CREATE TABLE user_scores (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES users(id) UNIQUE,
    total_points    INTEGER      NOT NULL DEFAULT 0,
    correct_scores  INTEGER      NOT NULL DEFAULT 0,
    correct_outcomes INTEGER     NOT NULL DEFAULT 0,
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);