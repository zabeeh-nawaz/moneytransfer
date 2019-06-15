package com.restful.moneytransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nawaz
 *
 */
@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount {

    /**
     * Primary key of the Bank Account
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * Account Number of the Bank Account
     */
    @Column(name = "ACCOUNT_NUMBER", unique = true)
    private String accountNumber;

    /**
     * Account holder name
     */
    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    /**
     * Balance in the account
     */
    @Column(name = "BALANCE")
    private Double balance;

    /**
     * Default constructor
     */
    public BankAccount() {

    }

    /**
     * @param accountNumber
     * @param accountHolderName
     * @param balance
     */
    public BankAccount(String accountNumber, String accountHolderName,
            Double balance) {
        setAccountNumber(accountNumber);
        setAccountHolderName(accountHolderName);
        setBalance(balance);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber
     *            the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the accountHolderName
     */
    public String getAccountHolderName() {
        return accountHolderName;
    }

    /**
     * @param accountHolderName
     *            the accountHolderName to set
     */
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    /**
     * @return the Balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * @param balance
     *            the Balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
