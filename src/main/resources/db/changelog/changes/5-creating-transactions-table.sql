CREATE TABLE Transactions
(
    transaction_id serial PRIMARY KEY,
    wallet_id      int          NOT NULL,
    amount         money        NOT NULL,
    category_id    int          NOT NULL,
    payee          varchar(255) NOT NULL,
    description    text         NOT NULL,
    date           date         NOT NULL,

    CONSTRAINT fk_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (wallet_id),
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
);