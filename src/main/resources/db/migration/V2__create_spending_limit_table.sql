CREATE TABLE spending_limit
(
    id           SERIAL PRIMARY KEY,
    limit_amount NUMERIC,
    category     VARCHAR(255),
    date_set     TIMESTAMP
);
