CREATE TABLE Monobank_Wallets
(
    monobank_wallet_id      serial PRIMARY KEY,
    user_id                 int          NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    actual_balance_in_cents bigint       NOT NULL,
    currency_id             int          NOT NULL,
    account_id              VARCHAR(255) NOT NULL,
    token                   VARCHAR(255) NOT NULL,
    last_sync_date          timestamp    NOT NULL,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_currency
        FOREIGN KEY (currency_id)
            REFERENCES currencies (currency_id)
);