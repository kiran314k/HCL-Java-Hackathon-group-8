-- Sample merchants
INSERT INTO merchant (merchant_id, merchant_name, account_number, currency, status, created_at) VALUES
(1, 'Merchant One', 'MERC-ACC-1', 'INR', 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'Merchant Two', 'MERC-ACC-2', 'INR', 'ACTIVE', CURRENT_TIMESTAMP);

-- Sample merchant accounts (merchant_id used as PK here to link account)
INSERT INTO merchant_account (merchant_id, account_number, currency, balance) VALUES
(1, 'MERC-ACC-1', 'INR', 100.00),
(2, 'MERC-ACC-2', 'INR', 50.00);

INSERT INTO transaction_log (payment_transaction_id, wallet_id, merchant_id, amount, type, status, initiated_at) VALUES
(1000, 10, 1, 10.50, 'SETTLEMENT', 'PENDING', CURRENT_TIMESTAMP);
