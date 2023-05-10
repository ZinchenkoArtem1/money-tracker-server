CREATE TABLE Categories
(
    category_id       serial PRIMARY KEY,
    name      VARCHAR(255) UNIQUE NOT NULL
);