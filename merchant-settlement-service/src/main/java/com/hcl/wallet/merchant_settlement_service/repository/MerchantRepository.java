package com.hcl.wallet.merchant_settlement_service.repository;

import com.hcl.wallet.merchant_settlement_service.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}

