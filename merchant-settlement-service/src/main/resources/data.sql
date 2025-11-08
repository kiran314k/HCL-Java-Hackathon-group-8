INSERT INTO merchant_settlement (merchant_id, settlement_id, transaction_id, amount) VALUES
(1, 100, 1000, 10.50),
(2, 100, 1001, 5.75);

INSERT INTO transaction_log (wallet_id, merchant_id, amount, type, status, initiated_at) VALUES
(10, 1, 10.50, 'SENT', 'PENDING', CURRENT_TIMESTAMP);
