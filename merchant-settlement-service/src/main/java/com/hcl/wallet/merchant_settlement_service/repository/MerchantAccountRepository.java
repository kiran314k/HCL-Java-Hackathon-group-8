package com.hcl.wallet.merchant_settlement_service.repository;

import com.hcl.wallet.merchant_settlement_service.model.MerchantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, Long> {
}

