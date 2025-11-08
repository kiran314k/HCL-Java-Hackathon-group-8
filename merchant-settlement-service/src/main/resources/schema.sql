CREATE TABLE merchant_settlement (
  merchant_settlement_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  merchant_id BIGINT UNSIGNED NOT NULL,
  settlement_id BIGINT UNSIGNED NOT NULL,
  transaction_id BIGINT UNSIGNED NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transaction_log (
  txn_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  payment_transaction_id BIGINT UNSIGNED,
  wallet_id BIGINT UNSIGNED,
  merchant_id BIGINT UNSIGNED,
  amount DECIMAL(15,2),
  type VARCHAR(20),
  status VARCHAR(20),
  initiated_at DATETIME,
  completed_at DATETIME,
  remarks TEXT
);

CREATE TABLE merchant_account (
  merchant_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  account_number VARCHAR(64),
  currency VARCHAR(3),
  balance DECIMAL(18,2) DEFAULT 0.00
);

CREATE TABLE merchant (
  merchant_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  merchant_name VARCHAR(255),
  account_number VARCHAR(64),
  currency VARCHAR(3),
  status VARCHAR(20),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
