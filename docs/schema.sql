-- ============================================================
-- WALLET PAYMENT SYSTEM SCHEMA (MySQL auto-increment version)
-- ============================================================
-- Converted from a UUID-based schema to use BIGINT UNSIGNED AUTO_INCREMENT
-- All primary keys are BIGINT UNSIGNED AUTO_INCREMENT and foreign keys updated to BIGINT UNSIGNED.

-- ========== CUSTOMER ==========
CREATE TABLE customer (
    customer_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== ACCOUNT ==========
CREATE TABLE account (
    account_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT UNSIGNED NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    bank_name VARCHAR(100),
    ifsc_code VARCHAR(20),
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    account_type ENUM('CUSTOMER','SYSTEM','MERCHANT') DEFAULT 'CUSTOMER',
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== WALLET ==========
CREATE TABLE wallet (
    wallet_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT UNSIGNED NOT NULL,
    account_id BIGINT UNSIGNED NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_wallet_account FOREIGN KEY (account_id)
        REFERENCES account(account_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== MERCHANT ==========
CREATE TABLE merchant (
    merchant_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    merchant_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PAYMENT TRANSACTION ==========
CREATE TABLE payment_transaction (
    transaction_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    wallet_id BIGINT UNSIGNED NOT NULL,
    merchant_id BIGINT UNSIGNED NOT NULL,
    product_name VARCHAR(255),
    product_cost DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status ENUM('INITIATED','VALIDATED','SUCCESS','FAILED','REFUNDED') DEFAULT 'INITIATED',
    initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME NULL,
    remarks TEXT,
    CONSTRAINT fk_payment_wallet FOREIGN KEY (wallet_id)
        REFERENCES wallet(wallet_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_payment_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchant(merchant_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== TRANSACTION LEDGER (renamed from `transaction`) ==========
-- Tracks money flow between customer and merchant
CREATE TABLE transaction_log (
    txn_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT UNSIGNED NOT NULL,
    merchant_id BIGINT UNSIGNED NOT NULL,
    payment_transaction_id BIGINT UNSIGNED NULL,
    sender_wallet_id BIGINT UNSIGNED NULL,
    receiver_account_id BIGINT UNSIGNED NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    type ENUM('SENT','RECEIVED','REFUND','TOPUP') NOT NULL,
    status ENUM('PENDING','COMPLETED','FAILED') DEFAULT 'PENDING',
    initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME NULL,
    remarks TEXT,
    CONSTRAINT fk_txn_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_txn_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchant(merchant_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_txn_payment FOREIGN KEY (payment_transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_txn_sender_wallet FOREIGN KEY (sender_wallet_id)
        REFERENCES wallet(wallet_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_txn_receiver_account FOREIGN KEY (receiver_account_id)
        REFERENCES account(account_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== SETTLEMENT ==========
CREATE TABLE settlement (
    settlement_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    status ENUM('PENDING','IN_PROGRESS','COMPLETED','FAILED') DEFAULT 'PENDING',
    total_amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    settlement_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== MERCHANT SETTLEMENT ==========
CREATE TABLE merchant_settlement (
    merchant_settlement_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT UNSIGNED NOT NULL,
    settlement_id BIGINT UNSIGNED NOT NULL,
    transaction_id BIGINT UNSIGNED NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ms_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchant(merchant_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ms_settlement FOREIGN KEY (settlement_id)
        REFERENCES settlement(settlement_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ms_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== NOTIFICATION ==========
CREATE TABLE notification (
    notification_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    transaction_id BIGINT UNSIGNED NULL,
    recipient_type ENUM('CUSTOMER','MERCHANT') NOT NULL,
    recipient_id BIGINT UNSIGNED NOT NULL,
    channel ENUM('EMAIL','SMS','PUSH') DEFAULT NULL,
    message TEXT,
    status ENUM('SENT','PENDING','FAILED') DEFAULT 'SENT',
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== AUDIT LOG ==========
CREATE TABLE audit_log (
    audit_id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    transaction_id BIGINT UNSIGNED NULL,
    action VARCHAR(100) NOT NULL,
    performed_by VARCHAR(100),
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    CONSTRAINT fk_audit_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ========== INDEXES ==========
CREATE INDEX idx_wallet_customer_id ON wallet(customer_id);
CREATE INDEX idx_wallet_account_id ON wallet(account_id);
CREATE INDEX idx_payment_wallet_id ON payment_transaction(wallet_id);
CREATE INDEX idx_payment_merchant_id ON payment_transaction(merchant_id);
CREATE INDEX idx_txn_customer_id ON transaction_log(customer_id);
CREATE INDEX idx_txn_merchant_id ON transaction_log(merchant_id);
CREATE INDEX idx_txn_payment_id ON transaction_log(payment_transaction_id);
CREATE INDEX idx_ms_merchant_id ON merchant_settlement(merchant_id);
CREATE INDEX idx_ms_settlement_id ON merchant_settlement(settlement_id);

-- End of schema
