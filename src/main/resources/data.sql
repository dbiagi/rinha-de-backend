GRANT ALL PRIVILEGES ON DATABASE rinha TO rinha;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO rinha;

CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    debit_limit BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    amount BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,
    account_id INT NOT NULL,
    description VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE INDEX idx_transaction_account_id ON transactions (account_id);

INSERT INTO account (debit_limit) VALUES (100000);
INSERT INTO account (debit_limit) VALUES (80000);
INSERT INTO account (debit_limit) VALUES (1000000);
INSERT INTO account (debit_limit) VALUES (10000000);
INSERT INTO account (debit_limit) VALUES (500000);
