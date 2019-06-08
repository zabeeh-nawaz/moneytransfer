package com.restful.moneytransfer.service;

import static com.restful.moneytransfer.App.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.restful.moneytransfer.constants.Constants;
import com.restful.moneytransfer.dao.MoneyTransferDao;
import com.restful.moneytransfer.dao.MoneyTransferDaoImpl;
import com.restful.moneytransfer.entity.BankAccount;
import com.restful.moneytransfer.entity.Transaction;
import com.restful.moneytransfer.exception.MissingResourceException;
import com.restful.moneytransfer.model.Account;
import com.restful.moneytransfer.model.Transfer;

/**
 * @author Nawaz
 *
 */
public final class MoneyTransferServiceImpl implements MoneyTransferService {

    /**
     * Available Balance of second Account
     */
    private static final double ACCOUNT_2_AVAILABLE_BALANCE = 5000.00;

    /**
     * Available Balance of first Account
     */
    private static final double ACCOUNT_1_AVAILABLE_BALANCE = 20000.00;

    /* Static Block that initializes EntityManagerFactory
     and creates sample data */
    {
        emf = Persistence.createEntityManagerFactory("MoneyTransferPU");
        initAccounts();
    }

    private MoneyTransferServiceImpl() {
    }

    /**
     *  Reference to the service instance
     */
    private static MoneyTransferService service;

    /**
     * Thread Pool Executor
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(
            Constants.THREAD_POOL_SIZE);

    /**
     * The EntityManagerFactory
     */
    private EntityManagerFactory emf;

    /**
     * The EntityManager
     */
    private EntityManager em;

    /**
     * @return reference to service instance
     */
    public static MoneyTransferService getService() {
        LOGGER.trace("MoneyTransferServiceImpl.getService has been invoked");
        if (service == null) {
            service = new MoneyTransferServiceImpl();
        }
        return service;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#createAccount(
     * com.restful.moneytransfer.model.Account)
     */
    @Override
    public Long createAccount(final Account account) {
        LOGGER.trace("MoneyTransferServiceImpl.createAccount has been invoked");
        BankAccount bankAccount = new BankAccount(account.getAccountNumber(),
                account.getName(), account.getBalance());
        getDAO().createAccount(bankAccount);
        closeEntityManager();
        return bankAccount.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#getAccount(java.
     * lang.Long)
     */
    @Override
    public Account getAccount(final Long id) throws MissingResourceException {
        LOGGER.trace("MoneyTransferServiceImpl.getAccount has been invoked");
        BankAccount bankAccount = getDAO().getAccount(id);
        closeEntityManager();
        if (bankAccount == null) {
            String message = "Account not found for id " + id;
            throw new MissingResourceException(message);
        }
        return new Account(bankAccount.getId(),
                bankAccount.getAccountHolderName(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.restful.moneytransfer.service.MoneyTransferService#getAccounts()
     */
    @Override
    public List<Account> getAccounts() {
        LOGGER.trace("MoneyTransferServiceImpl.getAccounts has been invoked");
        List<Account> accounts = new ArrayList<>();
        List<BankAccount> bankAccounts = getDAO().getAccounts();
        closeEntityManager();
        bankAccounts.forEach(
                bankAccount -> accounts.add(new Account(bankAccount.getId(),
                        bankAccount.getAccountHolderName(),
                        bankAccount.getAccountNumber(),
                        bankAccount.getBalance())));
        return accounts;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#deleteAccount(java
     * .lang.Long)
     */
    @Override
    public void deleteAccount(final Long id) throws MissingResourceException {
        LOGGER.trace("MoneyTransferServiceImpl.deleteAccount has been invoked");
        getDAO().deleteAccount(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#createTransaction(
     * java.lang.Double, java.lang.String, java.lang.String)
     */
    @Override
    public Long createTransaction(final Double amount,
            final String senderAccountNo,
            final String receiverAccountNo) throws Throwable {
        LOGGER.trace(
                "MoneyTransferServiceImpl.createTransaction has been invoked");
        Long transactionId;
        Callable<Long> task = () -> {
            EntityManager em = null;
            try {
                em = emf.createEntityManager();
                MoneyTransferDao dao = new MoneyTransferDaoImpl(em);
                return dao.createTransaction(amount, senderAccountNo,
                        receiverAccountNo);
            } finally {
                if (em != null) {
                    em.close();
                    em = null;
                }
            }
        };
        Future<Long> future = executor.submit(task);
        try {
            transactionId = future.get();
        } catch (ExecutionException e) {
            LOGGER.error("MoneyTransferServiceImpl.createTransaction "
                    + "incurred ExecutionException -> " + e);
            throw e.getCause();
        }
        return transactionId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#getTransfer(java.
     * lang.Long)
     */
    @Override
    public Transfer getTransfer(final Long id) throws MissingResourceException {
        LOGGER.trace("MoneyTransferServiceImpl.getTransfer has been invoked");
        Transaction transaction = getDAO().getTransaction(id);
        closeEntityManager();
        if (transaction == null) {
            String message = "Transaction not found for id " + id;
            throw new MissingResourceException(message);
        }
        return new Transfer(transaction.getId(),
                transaction.getTransferAmount(),
                transaction.getSenderAccountNumber(),
                transaction.getReceiverAccountNumber());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#getTransfers()
     */
    @Override
    public List<Transfer> getTransfers() {
        LOGGER.trace("MoneyTransferServiceImpl.getTransfers has been invoked");
        List<Transfer> transfers = new ArrayList<>();
        List<Transaction> transactions = getDAO().getTransactions();
        closeEntityManager();
        transactions.forEach(
                transaction -> transfers.add(new Transfer(transaction.getId(),
                        transaction.getTransferAmount(),
                        transaction.getSenderAccountNumber(),
                        transaction.getReceiverAccountNumber())));
        return transfers;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.restful.moneytransfer.service.MoneyTransferService#deleteTransfer(
     * java.lang.Long)
     */
    @Override
    public void deleteTransfer(final Long id) throws MissingResourceException {
        LOGGER.trace(
                "MoneyTransferServiceImpl.deleteTransfer has been invoked");
        getDAO().deleteTransaction(id);
    }

    /**
     * Sets up initial data
     */
    public void initAccounts() {
        LOGGER.trace("MoneyTransferServiceImpl.initAccounts has been invoked");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        BankAccount accountOne = new BankAccount("5012345678", "Mike",
                new Double(ACCOUNT_1_AVAILABLE_BALANCE));
        BankAccount accountTwo = new BankAccount("6012345678", "Bob",
                new Double(ACCOUNT_2_AVAILABLE_BALANCE));
        em.persist(accountOne);
        em.persist(accountTwo);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * @return instance of DAO class
     */
    public MoneyTransferDao getDAO() {
        LOGGER.trace("MoneyTransferServiceImpl.getDAO has been invoked");
        em = emf.createEntityManager();
        return new MoneyTransferDaoImpl(em);
    }

    /**
     * Checks and closes entity manager
     */
    private void closeEntityManager() {
        LOGGER.trace(
                "MoneyTransferServiceImpl.closeEntityManager has been invoked");
        if (em != null) {
            em.close();
            em = null;
        }
    }

}
