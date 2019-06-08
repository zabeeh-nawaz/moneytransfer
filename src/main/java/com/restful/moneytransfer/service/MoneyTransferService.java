package com.restful.moneytransfer.service;

import java.util.List;

import com.restful.moneytransfer.exception.MissingResourceException;
import com.restful.moneytransfer.model.Account;
import com.restful.moneytransfer.model.Transfer;

/**
 * @author Nawaz
 *
 */
public interface MoneyTransferService {

    /**
     * Creates a transaction for the money transfer
     * @param amount
     * @param string
     * @param string2
     * @return
     * @throws Throwable
     */
    Long createTransaction(Double amount, String string, String string2)
            throws Throwable;

    /**
     * Creates an Account
     * @param account
     * @return
     */
    Long createAccount(Account account);

    /**
     * @return
     *          the list of accounts
     */
    List<Account> getAccounts();

    /**
     * @param id
     *           the id of the Account
     * @return
     *          the Account for the id
     * @throws MissingResourceException
     */
    Account getAccount(Long id) throws MissingResourceException;

    /**
     *
     * @param id
     *           the id of Transaction
     * @return
     *          the transfer Transaction for the id
     * @throws MissingResourceException
     */
    Transfer getTransfer(Long id) throws MissingResourceException;

    /**
     * @return
     *         List of transfers
     */
    List<Transfer> getTransfers();

    /**
     * Deletes the account with the given id
     * @param id
     *           the id of the Account
     * @throws MissingResourceException
     */
    void deleteAccount(Long id) throws MissingResourceException;

    /**
     * Deletes the transaction with the given id
     * @param id
     *           the id of the transfer Transaction
     * @throws MissingResourceException
     */
    void deleteTransfer(Long id) throws MissingResourceException;

}
