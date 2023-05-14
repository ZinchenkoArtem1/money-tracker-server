CREATE TABLE Wallets
(
    wallet_id               serial PRIMARY KEY,
    user_id                 int          NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    actual_balance_in_cents bigint       NOT NULL,
    currency_id             int          NOT NULL,
    wallet_type             VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_currency
        FOREIGN KEY (currency_id)
            REFERENCES currencies (currency_id)
);