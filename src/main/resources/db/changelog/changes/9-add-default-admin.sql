insert into Users (email, password, first_name, last_name, role)
values ('admin@email.com', '$2a$12$mFQItzj8oHK79kL.hBpUterCLXt6v07Rnxi.RDfB2R7W7IQqh2xxa', 'admin', 'admin', 'ADMIN');

insert into Users (email, password, first_name, last_name, role)
values ('user@email.com', '$2a$12$XY5cT7L2IHibep9URxLt7uRo1cCvKDQLpxqnt18j2wLmmrZVY/YRy', 'user', 'user', 'USER');

insert into Wallets (user_id, name, actual_balance_in_cents, currency_id, wallet_type)
values (2, 'USD_MANUAL', 1000000, 1, 'MANUAL');

insert into Wallets (user_id, name, actual_balance_in_cents, currency_id, wallet_type)
values (2, 'EUR_MANUAL', 1000000, 2, 'MANUAL');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (1, -1000, 7, 'Products: apple, water', '2023-05-18 01:23:25.000000');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (1, -2000, 8, 'Taxi', '2023-05-17 01:23:25.000000');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (1, 5000, 2, 'Work salary', '2023-05-16 01:23:25.000000');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (2, -2000, 12, 'Ticket to mount', '2023-05-12 01:23:25.000000');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (2, -3000, 11, 'Berska', '2023-05-11 01:23:25.000000');

insert into Transactions(wallet_id, amount_in_cents, category_id, description, created_at)
values (2, 10000, 2, 'Work salary', '2023-05-14 01:23:25.000000');