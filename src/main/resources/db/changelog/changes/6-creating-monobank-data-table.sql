CREATE TABLE Monobank_Data
(
    monobank_data_id serial PRIMARY KEY,
    account_id       VARCHAR(255)             NOT NULL,
    token            VARCHAR(255)             NOT NULL,
    last_sync_date   TIMESTAMP WITH TIME ZONE NOT NULL,
    wallet_id        int                      NOT NULL,

    CONSTRAINT fk_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (wallet_id) ON DELETE CASCADE
);