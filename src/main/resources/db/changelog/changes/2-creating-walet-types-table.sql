CREATE TABLE Wallet_Types
(
    wallet_type_id       serial PRIMARY KEY,
    name      VARCHAR(255) UNIQUE NOT NULL
);