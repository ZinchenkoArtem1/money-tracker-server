CREATE TABLE Monobank_Transactions
(
    monobank_transaction_id serial PRIMARY KEY,
    monobank_wallet_id      int       NOT NULL,
    amount_in_cents         bigint    NOT NULL,
    category_id             int       NOT NULL,
    description             text      NOT NULL,
    created_at              timestamp NOT NULL,

    CONSTRAINT fk_wallet
        FOREIGN KEY (monobank_wallet_id)
            REFERENCES monobank_wallets (monobank_wallet_id),
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
);