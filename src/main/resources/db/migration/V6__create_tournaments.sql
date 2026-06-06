CREATE TABLE tournaments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status      VARCHAR(20)  NOT NULL DEFAULT 'UPCOMING',
    has_groups  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);