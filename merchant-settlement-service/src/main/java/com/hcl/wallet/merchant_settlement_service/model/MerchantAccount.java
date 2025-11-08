package com.hcl.wallet.merchant_settlement_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "merchant_account")
public class MerchantAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    public MerchantAccount() {
    }

    public MerchantAccount(Long id, String accountNumber, String currency, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MerchantAccount)) return false;
        MerchantAccount that = (MerchantAccount) o;
        return Objects.equals(id, that.id) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(currency, that.currency) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, currency, balance);
    }
}

