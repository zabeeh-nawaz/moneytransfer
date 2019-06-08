package com.restful.moneytransfer.dao;

import java.util.List;

import com.restful.moneytransfer.entity.BankAccount;
import com.restful.moneytransfer.entity.Transaction;
import com.restful.moneytransfer.exception.MissingResourceException;

/**
 * @author Nawaz
 *
 */
public interface MoneyTransferDao {

    /**
     * Method to create Transaction
     *
     * @param amount
     * @param senderAccountNumber
     * @param receiverAccountNumber
     * @return
     * @throws Exception
     */
    Long createTransaction(Double amount, String senderAccountNumber,
            String receiverAccountNumber) throws Exception;

    /**
     * Method to create BankAccount
     *
     * @param bankAccount
     */
    void createAccount(BankAccount bankAccount);

    /**
     * @return Bank Accounts
     */
    List<BankAccount> getAccounts();

    /**
     * @param id of the Account
     * @return Bank Account
     */
    BankAccount getAccount(Long id);

    /**
     * @param id of the Transaction
     * @return Transaction
     */
    Transaction getTransaction(Long id);

    /**
     * @return Transactions
     */
    List<Transaction> getTransactions();

    /**
     * Deletes Account
     * @param id of the account to be deleted
     * @throws MissingResourceException
     */
    void deleteAccount(Long id) throws MissingResourceException;

    /**
     * Deletes Transaction
     * @param id of the transaction to be deleted
     * @throws MissingResourceException
     */
    void deleteTransaction(Long id) throws MissingResourceException;

}
