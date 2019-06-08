package com.restful.moneytransfer.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nawaz
 *
 */
@XmlRootElement(name = "accounts")
public class Accounts {

    /**
     * List of accounts
     */
    private List<Account> accounts;

    /**
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts
     *            the accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
