CREATE TABLE exchange_rate
(
    id            SERIAL PRIMARY KEY,
    currency_pair VARCHAR(255),
    rate          NUMERIC,
    rate_date     TIMESTAMP
);
