package com.hcl.wallet.service;

import com.hcl.wallet.entity.Account;
import com.hcl.wallet.entity.Customer;
import com.hcl.wallet.entity.Wallet;
import com.hcl.wallet.exception.InvalidInputException;
import com.hcl.wallet.repository.AccountRepository;
import com.hcl.wallet.repository.CustomerRepository;
import com.hcl.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author srinivasa
 */
@Service
public class WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;

    public WalletService(CustomerRepository customerRepository,
                         AccountRepository accountRepository,
                         WalletRepository walletRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Wallet createWallet(Long customerId, Long accountId, String currency) {
        log.info(" Creating wallet for customerId={} and accountId={} with currency={}", customerId, accountId, currency);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn(" Customer not found: {}", customerId);
                    return new InvalidInputException("Customer not found with ID: " + customerId);
                });

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn(" Account not found: {}", accountId);
                    return new InvalidInputException("Account not found with ID: " + accountId);
                });

        if (!account.getCustomer().getCustomerId().equals(customerId)) {
            log.error(" Account {} does not belong to Customer {}", accountId, customerId);
            throw new InvalidInputException("Account does not belong to this customer");
        }

        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setAccount(account);
        wallet.setCurrency(currency);
        wallet.setBalance(account.getBalance());
        wallet.setStatus("ACTIVE");

        Wallet savedWallet = walletRepository.save(wallet);
        log.info(" Wallet created successfully with walletId={} | Initial Balance={}", savedWallet.getWalletId(), savedWallet.getBalance());

        return savedWallet;
    }

    public Wallet getWallet(Long walletId) {
        log.info(" Fetching wallet details for walletId={}", walletId);
        return walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.warn(" Wallet not found: {}", walletId);
                    return new InvalidInputException("Wallet not found with ID: " + walletId);
                });
    }
}