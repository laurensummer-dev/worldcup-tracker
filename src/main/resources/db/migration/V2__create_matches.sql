CREATE TABLE matches (
    id              BIGSERIAL PRIMARY KEY,
    home_team       VARCHAR(100) NOT NULL,
    away_team       VARCHAR(100) NOT NULL,
    kick_off_time   TIMESTAMP    NOT NULL,
    home_score      INTEGER,
    away_score      INTEGER,
    status          VARCHAR(20)  NOT NULL DEFAULT 'SCHEDULED',
    scored          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);