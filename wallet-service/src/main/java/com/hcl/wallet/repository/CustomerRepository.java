package com.hcl.wallet.repository;

import com.hcl.wallet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
/**
 * @author srinivasa
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}