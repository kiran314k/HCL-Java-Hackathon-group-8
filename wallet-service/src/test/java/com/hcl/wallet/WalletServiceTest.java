package com.hcl.wallet;

import com.hcl.wallet.entity.Account;
import com.hcl.wallet.entity.Customer;
import com.hcl.wallet.entity.Wallet;
import com.hcl.wallet.exception.InvalidInputException;
import com.hcl.wallet.repository.AccountRepository;
import com.hcl.wallet.repository.CustomerRepository;
import com.hcl.wallet.repository.WalletRepository;
import com.hcl.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Customer customer;
    private Account account;
    private Wallet wallet;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Rahul Sharma");
        customer.setEmail("rahul@example.com");
        customer.setPhone("9876543210");

        account = new Account();
        account.setAccountId(2L);
        account.setCustomer(customer);
        account.setAccountNumber("ACCT123456789");
        account.setBalance(new BigDecimal("500.00"));
        account.setCurrency("USD");

        wallet = new Wallet();
        wallet.setWalletId(3L);
        wallet.setCustomer(customer);
        wallet.setAccount(account);
        wallet.setCurrency("USD");
        wallet.setBalance(account.getBalance());
        wallet.setStatus("ACTIVE");
    }

    // ✅ Happy path: wallet creation success
    @Test
    void testCreateWallet_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet created = walletService.createWallet(1L, 2L, "USD");

        assertNotNull(created);
        assertEquals(1L, created.getCustomer().getCustomerId());
        assertEquals(2L, created.getAccount().getAccountId());
        assertEquals("USD", created.getCurrency());
        assertEquals(new BigDecimal("500.00"), created.getBalance());

        verify(walletRepository, times(1)).save(any(Wallet.class));
        verify(customerRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
    }

    //  Customer not found
    @Test
    void testCreateWallet_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> walletService.createWallet(1L, 2L, "USD")
        );

        assertEquals("Customer not found with ID: 1", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(accountRepository, never()).findById(any());
    }

    //  Account not found
    @Test
    void testCreateWallet_AccountNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> walletService.createWallet(1L, 2L, "USD")
        );

        assertEquals("Account not found with ID: 2", exception.getMessage());
        verify(accountRepository, times(1)).findById(2L);
    }

    //  Account does not belong to Customer
    @Test
    void testCreateWallet_AccountMismatch() {
        Customer otherCustomer = new Customer();
        otherCustomer.setCustomerId(99L);
        account.setCustomer(otherCustomer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> walletService.createWallet(1L, 2L, "USD")
        );

        assertEquals("Account does not belong to this customer", exception.getMessage());
    }

    //  Get wallet success
    @Test
    void testGetWallet_Success() {
        when(walletRepository.findById(3L)).thenReturn(Optional.of(wallet));

        Wallet found = walletService.getWallet(3L);

        assertNotNull(found);
        assertEquals(3L, found.getWalletId());
        verify(walletRepository, times(1)).findById(3L);
    }

    //  Wallet not found
    @Test
    void testGetWallet_NotFound() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> walletService.getWallet(99L)
        );

        assertEquals("Wallet not found with ID: 99", exception.getMessage());
        verify(walletRepository, times(1)).findById(99L);
    }
}
