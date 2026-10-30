CREATE TABLE tb_characters
(
    id              BINARY(16)   NOT NULL,
    name            VARCHAR(100) NOT NULL,
    species         VARCHAR(50)  NOT NULL,
    character_class VARCHAR(30)  NOT NULL,
    background      VARCHAR(80)  NOT NULL,
    level           SMALLINT     NOT NULL CHECK (level BETWEEN 1 AND 20),
    strength        SMALLINT     NOT NULL CHECK (strength BETWEEN 1 AND 30),
    dexterity       SMALLINT     NOT NULL CHECK (dexterity BETWEEN 1 AND 30),
    constitution    SMALLINT     NOT NULL CHECK (constitution BETWEEN 1 AND 30),
    intelligence    SMALLINT     NOT NULL CHECK (intelligence BETWEEN 1 AND 30),
    wisdom          SMALLINT     NOT NULL CHECK (wisdom BETWEEN 1 AND 30),
    charisma        SMALLINT     NOT NULL CHECK (charisma BETWEEN 1 AND 30),
    created_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at      DATETIME(6),
    version         BIGINT       NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);