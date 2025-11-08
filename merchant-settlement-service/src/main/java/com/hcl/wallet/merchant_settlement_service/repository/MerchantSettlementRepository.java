package com.hcl.wallet.merchant_settlement_service.repository;

import com.hcl.wallet.merchant_settlement_service.model.MerchantSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantSettlementRepository extends JpaRepository<MerchantSettlement, Long> {

}
