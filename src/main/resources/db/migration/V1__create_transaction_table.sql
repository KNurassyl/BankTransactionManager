CREATE TABLE transaction
(
    id               SERIAL PRIMARY KEY,
    account_from     VARCHAR(255),
    account_to       VARCHAR(255),
    currency         VARCHAR(255),
    amount           NUMERIC,
    expense_category VARCHAR(255),
    transaction_date TIMESTAMP,
    limit_exceeded   BOOLEAN
);
