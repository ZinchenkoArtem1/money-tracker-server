CREATE TABLE Wallets
(
    wallet_id      serial PRIMARY KEY,
    user_id        int          NOT NULL,
    name           VARCHAR(255) NOT NULL,
    wallet_type_id int          NOT NULL,
    balance        money        NOT NULL,
    currency_id    int          NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users(user_id),
    CONSTRAINT fk_wallet_type
        FOREIGN KEY(wallet_type_id)
            REFERENCES wallet_types(wallet_type_id),
    CONSTRAINT fk_currency
        FOREIGN KEY(currency_id)
            REFERENCES currencies(currency_id)
);