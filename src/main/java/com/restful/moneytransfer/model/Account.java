package com.restful.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nawaz
 *
 */
@XmlRootElement()
public class Account {

    /**
     * Identifier of the account
     */
    private Long id;

    /**
     * Name of the account holder
     */
    private String name;

    /**
     * Account number for the account
     */
    private String accountNumber;

    /**
     * Balance available on the account
     */
    private Double balance;

    /**
     * Default constructor
     */
    public Account() {
    }

    /**
     * @param id
     * @param name
     * @param accountNumber
     * @param balance
     */
    public Account(Long id, String name, String accountNumber,
            Double balance) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }


}
