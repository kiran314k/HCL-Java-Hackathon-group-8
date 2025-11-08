-- ============================================================
-- WALLET PAYMENT SYSTEM SCHEMA (FINAL VERSION)
-- ============================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ========== CUSTOMER ==========
CREATE TABLE customer (
    customer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========== ACCOUNT ==========
CREATE TABLE account (
    account_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    bank_name VARCHAR(100),
    ifsc_code VARCHAR(20),
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    account_type VARCHAR(20) DEFAULT 'CUSTOMER', -- CUSTOMER / SYSTEM / MERCHANT
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON DELETE CASCADE
);

-- ========== WALLET ==========
CREATE TABLE wallet (
    wallet_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL,
    account_id UUID NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_wallet_account FOREIGN KEY (account_id)
        REFERENCES account(account_id)
        ON DELETE CASCADE
);

-- ========== MERCHANT ==========
CREATE TABLE merchant (
    merchant_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    merchant_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========== PAYMENT TRANSACTION ==========
CREATE TABLE payment_transaction (
    transaction_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    wallet_id UUID NOT NULL,
    merchant_id UUID NOT NULL,
    product_name VARCHAR(255),
    product_cost DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) DEFAULT 'INITIATED',
    initiated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    remarks TEXT,
    CONSTRAINT fk_payment_wallet FOREIGN KEY (wallet_id)
        REFERENCES wallet(wallet_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_payment_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchant(merchant_id)
        ON DELETE CASCADE
);

-- ========== TRANSACTION LEDGER ==========
-- Tracks money flow between customer and merchant
CREATE TABLE transaction (
    txn_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL,
    merchant_id UUID NOT NULL,
    payment_transaction_id UUID,
    sender_wallet_id UUID,
    receiver_account_id UUID,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    type VARCHAR(20) NOT NULL, -- SENT / RECEIVED / REFUND / TOPUP
    status VARCHAR(20) DEFAULT 'PENDING',
    initiated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
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
);

-- ========== SETTLEMENT ==========
CREATE TABLE settlement (
    settlement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    settlement_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========== MERCHANT SETTLEMENT ==========
CREATE TABLE merchant_settlement (
    merchant_settlement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    merchant_id UUID NOT NULL,
    settlement_id UUID NOT NULL,
    transaction_id UUID NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ms_merchant FOREIGN KEY (merchant_id)
        REFERENCES merchant(merchant_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ms_settlement FOREIGN KEY (settlement_id)
        REFERENCES settlement(settlement_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ms_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE CASCADE
);

-- ========== NOTIFICATION ==========
CREATE TABLE notification (
    notification_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID,
    recipient_type VARCHAR(20) NOT NULL, -- CUSTOMER / MERCHANT
    recipient_id UUID NOT NULL,
    channel VARCHAR(20), -- EMAIL / SMS / PUSH
    message TEXT,
    status VARCHAR(20) DEFAULT 'SENT',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE SET NULL
);

-- ========== AUDIT LOG ==========
CREATE TABLE audit_log (
    audit_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID,
    action VARCHAR(100) NOT NULL,
    performed_by VARCHAR(100),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    CONSTRAINT fk_audit_transaction FOREIGN KEY (transaction_id)
        REFERENCES payment_transaction(transaction_id)
        ON DELETE SET NULL
);

-- ========== INDEXES ==========
CREATE INDEX idx_wallet_customer_id ON wallet(customer_id);
CREATE INDEX idx_wallet_account_id ON wallet(account_id);
CREATE INDEX idx_payment_wallet_id ON payment_transaction(wallet_id);
CREATE INDEX idx_payment_merchant_id ON payment_transaction(merchant_id);
CREATE INDEX idx_txn_customer_id ON transaction(customer_id);
CREATE INDEX idx_txn_merchant_id ON transaction(merchant_id);
CREATE INDEX idx_txn_payment_id ON transaction(payment_transaction_id);
CREATE INDEX idx_ms_merchant_id ON merchant_settlement(merchant_id);
CREATE INDEX idx_ms_settlement_id ON merchant_settlement(settlement_id);

-- ========== CHECK CONSTRAINTS ==========
ALTER TABLE wallet
  ADD CONSTRAINT chk_wallet_status CHECK (status IN ('ACTIVE','INACTIVE'));

ALTER TABLE account
  ADD CONSTRAINT chk_account_status CHECK (status IN ('ACTIVE','INACTIVE'));

ALTER TABLE payment_transaction
  ADD CONSTRAINT chk_payment_status CHECK (status IN ('INITIATED','VALIDATED','SUCCESS','FAILED','REFUNDED'));

ALTER TABLE settlement
  ADD CONSTRAINT chk_settlement_status CHECK (status IN ('PENDING','IN_PROGRESS','COMPLETED','FAILED'));

ALTER TABLE transaction
  ADD CONSTRAINT chk_txn_type CHECK (type IN ('SENT','RECEIVED','REFUND','TOPUP'));
