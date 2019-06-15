package com.restful.moneytransfer.dao;

import static com.restful.moneytransfer.App.LOGGER;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.restful.moneytransfer.entity.BankAccount;
import com.restful.moneytransfer.entity.Transaction;
import com.restful.moneytransfer.exception.InsufficientFundsException;
import com.restful.moneytransfer.exception.MissingResourceException;

/**
 * @author Nawaz
 *
 */
public class MoneyTransferDaoImpl implements MoneyTransferDao {

    /**
     * Constant defining that receiver object should be fetched first
     */
    public static final String RECEIVER_FIRST = "RECEIVER_FIRST";

    /**
     * Constant defining that sender object should be fetched first
     */
    public static final String SENDER_FIRST = "SENDER_FIRST";

    /**
     * Instance of Entity Manager
     */
    private EntityManager em;

    /**
     * @param em, instance of entity manager
     */
    public MoneyTransferDaoImpl(final EntityManager em) {
        this.em = em;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#createAccount(com.restful.
     * moneytransfer.entity.BankAccount)
     */
    @Override
    public void createAccount(final BankAccount bankAccount) throws Exception {
        LOGGER.trace("MoneyTransferDaoImpl.createAccount has been invoked");
        try {
            em.getTransaction().begin();
            em.persist(bankAccount);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#getAccount(java.lang.Long)
     */
    @Override
    public BankAccount getAccount(final Long id) {
        LOGGER.trace("MoneyTransferDaoImpl.getAccount has been invoked");
        BankAccount account = em.find(BankAccount.class, id);
        return account;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.restful.moneytransfer.dao.MoneyTransferDao#getAccounts()
     */
    @Override
    public List<BankAccount> getAccounts() {
        LOGGER.trace("MoneyTransferDaoImpl.getAccounts has been invoked");
        Query query = em.createQuery("SELECT account FROM BankAccount account");
        List<BankAccount> accounts = query.getResultList();
        return accounts;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#deleteAccount(java.lang.
     * Long)
     */
    @Override
    public void deleteAccount(final Long id) throws MissingResourceException {
        LOGGER.trace("MoneyTransferDaoImpl.deleteAccount has been invoked");
        BankAccount account = em.find(BankAccount.class, id);
        if (account == null) {
            String message = "Account not found for id " + id;
            throw new MissingResourceException(message);
        }
        try {
            em.getTransaction().begin();
            em.remove(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Exception in MoneyTransferDaoImpl.deleteAccount -> "
                    + e.getMessage());
            em.getTransaction().rollback();
            throw e;
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#createTransaction(java.
     * lang.Double, java.lang.String, java.lang.String)
     */
    @Override
    public Long createTransaction(final Double transferAmount,
            final String senderAccountNumber,
            final String receiverAccountNumber)
                    throws Exception {
        LOGGER.trace("MoneyTransferDaoImpl.createTransaction has been invoked");
        Transaction transaction = new Transaction(senderAccountNumber,
                receiverAccountNumber, transferAmount);

        try {
            em.getTransaction().begin();
            BankAccount sender = null;
            BankAccount receiver = null;

            String order = determineOrder(senderAccountNumber,
                    receiverAccountNumber);

            if (SENDER_FIRST.equals(order)) {
                sender = getBankAccountForUpdate(senderAccountNumber);
                receiver = getBankAccountForUpdate(receiverAccountNumber);
            } else {
                receiver = getBankAccountForUpdate(receiverAccountNumber);
                sender = getBankAccountForUpdate(senderAccountNumber);
            }

            if (sender.getBalance() < transferAmount) {
                throw new InsufficientFundsException();
            }

            // Deduct Amount from sender
            sender.setBalance(
                    sender.getBalance() - transferAmount);

            // Add Amount to receiver
            receiver.setBalance(
                    receiver.getBalance() + transferAmount);

            // Create a transaction record for reference
            em.persist(transaction);

            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(
                    "Exception in MoneyTransferDaoImpl.createTransaction -> "
                            + e.getMessage());
            em.getTransaction().rollback();
            throw e;
        }
        return transaction.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#getTransaction(java.lang.
     * Long)
     */
    @Override
    public Transaction getTransaction(final Long id) {
        LOGGER.trace("MoneyTransferDaoImpl.getTransaction has been invoked");
        Transaction transaction = em.find(Transaction.class, id);
        return transaction;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.restful.moneytransfer.dao.MoneyTransferDao#getTransactions()
     */
    @Override
    public List<Transaction> getTransactions() {
        LOGGER.trace("MoneyTransferDaoImpl.getTransactions has been invoked");
        Query query = em
                .createQuery("SELECT transaction FROM Transaction transaction");
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.dao.MoneyTransferDao#deleteTransaction(java.
     * lang.Long)
     */
    @Override
    public void deleteTransaction(final Long id)
            throws MissingResourceException {
        LOGGER.trace("MoneyTransferDaoImpl.deleteTransaction has been invoked");
        Transaction transaction = em.find(Transaction.class, id);
        if (transaction == null) {
            String message = "Transaction not found for id " + id;
            throw new MissingResourceException(message);
        }
        try {
            em.getTransaction().begin();
            em.remove(transaction);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(
                    "Exception in MoneyTransferDaoImpl.deleteTransaction -> "
                            + e.getMessage());
            em.getTransaction().rollback();
            throw e;
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }
    }

    /**
     * @param senderAccountNumber
     * @param receiverAccountNumber
     * @return
     */
    private static String determineOrder(final String senderAccountNumber,
            final String receiverAccountNumber) {
        LOGGER.trace("MoneyTransferDaoImpl.determineOrder has been invoked");
        if (senderAccountNumber.compareTo(receiverAccountNumber) > 0) {
            return SENDER_FIRST;
        }
        return RECEIVER_FIRST;
    }

    /*
     * Fetches with PESSIMISTIC_WRITE lock on object to update
     */
    /**
     * @param accountNumber
     * @return
     * @throws MissingResourceException
     */
    public BankAccount getBankAccountForUpdate(final String accountNumber)
            throws MissingResourceException {
        LOGGER.trace(
                "MoneyTransferDaoImpl.getBankAccountForUpdate "
                        + "has been invoked");
        try {
            Query query = em.createQuery(
                    "SELECT b FROM BankAccount b WHERE b.accountNumber = ?");
            query.setParameter(1, accountNumber);
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
            return (BankAccount) query.getSingleResult();
        } catch (NoResultException e) {
            String message = "Account with account number " + accountNumber
                    + " does not exist";
            LOGGER.error(
                    "Exception in MoneyTransferDaoImpl.getBankAccountForUpdate "
                            + "->" + message);
            throw new MissingResourceException(message);
        }
    }

}
