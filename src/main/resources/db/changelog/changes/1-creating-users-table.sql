CREATE TABLE Users
(
    user_id       serial PRIMARY KEY,
    username      VARCHAR(255) UNIQUE NOT NULL,
    registry_type VARCHAR(255)        NOT NULL
);