CREATE TABLE IF NOT EXISTS book (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    author          VARCHAR(255) NOT NULL,
    publisher       VARCHAR(255) NOT NULL,
    isbn            VARCHAR(13) UNIQUE,
    pages           INTEGER,
    release_date    DATE,
    language        VARCHAR(50),
    description     TEXT
);