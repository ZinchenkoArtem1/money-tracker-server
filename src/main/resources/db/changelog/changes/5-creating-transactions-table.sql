CREATE TABLE Transactions
(
    transaction_id      serial PRIMARY KEY,
    wallet_id           int                      NOT NULL,
    amount_in_cents     bigint                   NOT NULL,
    amount_in_cents_uah bigint                   NOT NULL,
    category_id         int                      NOT NULL,
    description         text                     NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (wallet_id) ON DELETE CASCADE,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
);