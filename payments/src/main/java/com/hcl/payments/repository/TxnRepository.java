package com.hcl.payments.repository;

import com.hcl.payments.entity.Txn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepository extends JpaRepository<Txn, Long> {
}