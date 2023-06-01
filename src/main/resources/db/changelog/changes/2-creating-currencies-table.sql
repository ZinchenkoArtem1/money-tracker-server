CREATE TABLE Currencies
(
    currency_id serial PRIMARY KEY,
    name_eng    VARCHAR(255) UNIQUE NOT NULL,
    name_ukr    VARCHAR(255) UNIQUE NULL,
    code        int UNIQUE          NOT NULL
);