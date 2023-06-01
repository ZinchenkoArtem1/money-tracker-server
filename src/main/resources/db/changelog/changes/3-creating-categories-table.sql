CREATE TABLE Categories
(
    category_id serial PRIMARY KEY,
    name        VARCHAR(255) NOT NULL
);

CREATE TABLE Category_Monobank
(
    category_monobank_id serial PRIMARY KEY,
    mcc                  int NOT NULL UNIQUE,
    category_id          int NOT NULL,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES Categories (category_id)
);

CREATE TABLE Category_Privatbank
(
    category_privatbank_id serial PRIMARY KEY,
    name                   varchar(255) NOT NULL UNIQUE,
    category_id            int          NOT NULL,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES Categories (category_id)
);